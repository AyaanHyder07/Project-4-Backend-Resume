package com.resume.dashboard.service.admin;

import com.resume.dashboard.dto.admin.AdminAssignSubscriptionRequest;
import com.resume.dashboard.dto.admin.AdminRevenueSummaryResponse;
import com.resume.dashboard.dto.admin.AdminSubscriptionUserSummaryResponse;
import com.resume.dashboard.dto.admin.AdminUserBillingDetailsResponse;
import com.resume.dashboard.entity.PaymentOrder;
import com.resume.dashboard.entity.PaymentStatus;
import com.resume.dashboard.entity.Subscription;
import com.resume.dashboard.entity.User;
import com.resume.dashboard.exception.ResourceNotFoundException;
import com.resume.dashboard.repository.PaymentOrderRepository;
import com.resume.dashboard.repository.ResumeRepository;
import com.resume.dashboard.repository.SubscriptionRepository;
import com.resume.dashboard.repository.UserRepository;
import com.resume.dashboard.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminBillingService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final SubscriptionService subscriptionService;

    public AdminBillingService(UserRepository userRepository,
                               ResumeRepository resumeRepository,
                               SubscriptionRepository subscriptionRepository,
                               PaymentOrderRepository paymentOrderRepository,
                               SubscriptionService subscriptionService) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentOrderRepository = paymentOrderRepository;
        this.subscriptionService = subscriptionService;
    }

    public List<AdminSubscriptionUserSummaryResponse> getUserSummaries() {
        return userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::buildSummary)
                .toList();
    }

    public AdminUserBillingDetailsResponse getUserDetails(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AdminUserBillingDetailsResponse response = new AdminUserBillingDetailsResponse();
        response.setUser(buildSummary(user));
        response.setActiveSubscription(subscriptionRepository.findByUserIdAndActiveTrue(userId).orElse(null));
        response.setSubscriptionHistory(subscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId));
        response.setPaymentHistory(paymentOrderRepository.findByUserIdOrderByCreatedAtDesc(userId));
        return response;
    }

    public Subscription assignSubscription(String userId, AdminAssignSubscriptionRequest request) {
        if (request.getPlan() == null) {
            throw new IllegalArgumentException("Plan is required.");
        }
        Instant explicitEnd = null;
        if (request.getDurationDays() != null && request.getDurationDays() > 0) {
            explicitEnd = Instant.now().plus(request.getDurationDays(), ChronoUnit.DAYS);
        }
        return subscriptionService.createSubscription(userId, request.getPlan(), request.getBillingCycle(), explicitEnd);
    }

    public AdminRevenueSummaryResponse getRevenueSummary() {
        List<PaymentOrder> paidOrders = paymentOrderRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(order -> order.getStatus() == PaymentStatus.PAID)
                .toList();

        long totalRevenue = paidOrders.stream().mapToLong(PaymentOrder::getAmountInSmallestUnit).sum();
        long monthlyRevenue = paidOrders.stream()
                .filter(order -> order.getBillingCycle() != null && order.getBillingCycle().name().equals("MONTHLY"))
                .mapToLong(PaymentOrder::getAmountInSmallestUnit)
                .sum();
        long yearlyRevenue = paidOrders.stream()
                .filter(order -> order.getBillingCycle() != null && order.getBillingCycle().name().equals("YEARLY"))
                .mapToLong(PaymentOrder::getAmountInSmallestUnit)
                .sum();

        Map<String, Long> revenueByPlan = new LinkedHashMap<>();
        Map<String, Long> ordersByPlan = new LinkedHashMap<>();
        for (PaymentOrder order : paidOrders) {
            String planKey = order.getTargetPlan() == null ? "UNKNOWN" : order.getTargetPlan().name();
            revenueByPlan.put(planKey, revenueByPlan.getOrDefault(planKey, 0L) + order.getAmountInSmallestUnit());
            ordersByPlan.put(planKey, ordersByPlan.getOrDefault(planKey, 0L) + 1L);
        }

        AdminRevenueSummaryResponse response = new AdminRevenueSummaryResponse();
        response.setTotalRevenueInSmallestUnit(totalRevenue);
        response.setTotalPaidOrders(paidOrders.size());
        response.setMonthlyRevenueInSmallestUnit(monthlyRevenue);
        response.setYearlyRevenueInSmallestUnit(yearlyRevenue);
        response.setCurrency(paidOrders.isEmpty() ? "INR" : Optional.ofNullable(paidOrders.get(0).getCurrency()).orElse("INR"));
        response.setRevenueByPlan(revenueByPlan);
        response.setOrdersByPlan(ordersByPlan);
        response.setRecentPayments(paidOrders.stream().limit(20).toList());
        return response;
    }

    private AdminSubscriptionUserSummaryResponse buildSummary(User user) {
        String userId = user.getId();
        List<Subscription> history = subscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        Subscription active = history.stream().filter(Subscription::isActive).findFirst().orElse(null);
        Subscription latest = active != null ? active : (history.isEmpty() ? null : history.get(0));
        List<PaymentOrder> payments = paymentOrderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(order -> order.getStatus() == PaymentStatus.PAID)
                .toList();

        AdminSubscriptionUserSummaryResponse summary = new AdminSubscriptionUserSummaryResponse();
        summary.setUserId(userId);
        summary.setUsername(user.getUsername());
        summary.setRole(user.getRole());
        summary.setFreePlanConsumed(user.isFreePlanConsumed());
        summary.setJoinedAt(user.getCreatedAt());
        summary.setLastLogin(user.getLastLogin());
        summary.setPortfolioCount(resumeRepository.countByUserId(userId));
        summary.setPublishedPortfolioCount(resumeRepository.countByUserIdAndPublishedTrue(userId));
        summary.setCurrentPlan(latest != null ? latest.getPlan() : null);
        summary.setSubscriptionActive(active != null && (active.getEndDate() == null || active.getEndDate().isAfter(Instant.now())));
        summary.setSubscriptionEndDate(active != null ? active.getEndDate() : (latest != null ? latest.getEndDate() : null));
        summary.setBillingCycle(latest != null && latest.getBillingCycle() != null ? latest.getBillingCycle().name() : "-");
        summary.setTotalPaidOrders(payments.size());
        summary.setTotalRevenueInSmallestUnit(payments.stream().mapToLong(PaymentOrder::getAmountInSmallestUnit).sum());
        if (!payments.isEmpty()) {
            summary.setLatestDisplayAmount(payments.get(0).getDisplayAmount());
            summary.setLatestPaymentAt(payments.get(0).getCreatedAt());
        }
        return summary;
    }
}
