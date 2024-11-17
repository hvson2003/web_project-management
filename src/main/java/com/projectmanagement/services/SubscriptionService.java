package com.projectmanagement.services;

import com.projectmanagement.models.PlanType;
import com.projectmanagement.models.Subscription;
import com.projectmanagement.models.User;

public interface SubscriptionService {
    Subscription createSubscription(User user);

    Subscription getUsersSubscription(Long userID) throws Exception;

    Subscription updateSubscription(Long userId, PlanType planType);

    boolean isValid(Subscription subscription);
}
