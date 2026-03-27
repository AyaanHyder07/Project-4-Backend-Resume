package com.resume.dashboard.dto.admin;

import com.resume.dashboard.entity.PaymentOrder;

import java.util.List;
import java.util.Map;

public class AdminRevenueSummaryResponse {

    private long totalRevenueInSmallestUnit;
    private long totalPaidOrders;
    private long monthlyRevenueInSmallestUnit;
    private long yearlyRevenueInSmallestUnit;
    private String currency;
    private Map<String, Long> revenueByPlan;
    private Map<String, Long> ordersByPlan;
    private List<PaymentOrder> recentPayments;

    public long getTotalRevenueInSmallestUnit() { return totalRevenueInSmallestUnit; }
    public void setTotalRevenueInSmallestUnit(long totalRevenueInSmallestUnit) { this.totalRevenueInSmallestUnit = totalRevenueInSmallestUnit; }
    public long getTotalPaidOrders() { return totalPaidOrders; }
    public void setTotalPaidOrders(long totalPaidOrders) { this.totalPaidOrders = totalPaidOrders; }
    public long getMonthlyRevenueInSmallestUnit() { return monthlyRevenueInSmallestUnit; }
    public void setMonthlyRevenueInSmallestUnit(long monthlyRevenueInSmallestUnit) { this.monthlyRevenueInSmallestUnit = monthlyRevenueInSmallestUnit; }
    public long getYearlyRevenueInSmallestUnit() { return yearlyRevenueInSmallestUnit; }
    public void setYearlyRevenueInSmallestUnit(long yearlyRevenueInSmallestUnit) { this.yearlyRevenueInSmallestUnit = yearlyRevenueInSmallestUnit; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Map<String, Long> getRevenueByPlan() { return revenueByPlan; }
    public void setRevenueByPlan(Map<String, Long> revenueByPlan) { this.revenueByPlan = revenueByPlan; }
    public Map<String, Long> getOrdersByPlan() { return ordersByPlan; }
    public void setOrdersByPlan(Map<String, Long> ordersByPlan) { this.ordersByPlan = ordersByPlan; }
    public List<PaymentOrder> getRecentPayments() { return recentPayments; }
    public void setRecentPayments(List<PaymentOrder> recentPayments) { this.recentPayments = recentPayments; }
}
