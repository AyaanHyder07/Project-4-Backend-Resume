package com.resume.dashboard.entity;

public enum PaymentStatus {
    PENDING,  // Order created, waiting for payment confirmation
    PAID,     // Payment confirmed — subscription has been upgraded
    FAILED,   // Payment failed or was rejected
    EXPIRED   // Order was never confirmed within the 30-minute window
}