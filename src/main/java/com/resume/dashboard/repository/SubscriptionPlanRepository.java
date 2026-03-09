package com.resume.dashboard.repository;

import com.resume.dashboard.entity.PlanType;
import com.resume.dashboard.entity.SubscriptionPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanRepository extends MongoRepository<SubscriptionPlan, String> {

    // Used by SubscriptionService.applyPlanLimits() to load limits dynamically
    Optional<SubscriptionPlan> findByPlanType(PlanType planType);

    // Used by pricing page — only show active plans, sorted by displayOrder
    List<SubscriptionPlan> findByIsActiveTrueOrderByDisplayOrderAsc();
}