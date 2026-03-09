package com.resume.dashboard.repository;

import com.resume.dashboard.entity.PaymentOrder;
import com.resume.dashboard.entity.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository extends MongoRepository<PaymentOrder, String> {

    Optional<PaymentOrder> findByIdAndUserId(String id, String userId);

    List<PaymentOrder> findByUserIdOrderByCreatedAtDesc(String userId);

    // Prevents duplicate pending orders for the same user
    boolean existsByUserIdAndStatus(String userId, PaymentStatus status);
}