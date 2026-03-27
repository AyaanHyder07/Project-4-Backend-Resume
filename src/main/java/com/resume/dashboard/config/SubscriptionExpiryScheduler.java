package com.resume.dashboard.config;

import com.resume.dashboard.service.SubscriptionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionExpiryScheduler {

    private final SubscriptionService subscriptionService;

    public SubscriptionExpiryScheduler(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void expireEndedSubscriptions() {
        subscriptionService.expireEndedSubscriptions();
    }
}
