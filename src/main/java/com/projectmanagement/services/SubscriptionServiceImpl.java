package com.projectmanagement.services;

import com.projectmanagement.models.PlanType;
import com.projectmanagement.models.Subscription;
import com.projectmanagement.models.User;
import com.projectmanagement.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createSubscription(User user) {
        Subscription subscription = new Subscription();

        subscription.setUser(user);
        subscription.setSubscriptionsStartDate(LocalDate.now());
        subscription.setGetSubscriptionsEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlanType(PlanType.FREE);

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getUsersSubscription(Long userID) throws Exception {
        Subscription subscription = subscriptionRepository.findByUserId(userID);
        if(!isValid(subscription)) {
            subscription.setPlanType(PlanType.FREE);
            subscription.setGetSubscriptionsEndDate(LocalDate.now().plusMonths(12));
            subscription.setSubscriptionsStartDate(LocalDate.now());
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscription(Long userId, PlanType planType) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        subscription.setPlanType(planType);
        subscription.setSubscriptionsStartDate(LocalDate.now());
        if(planType.equals(PlanType.ANNUALLY)) {
            subscription.setGetSubscriptionsEndDate(LocalDate.now().plusMonths(12));
        } else {
            subscription.setGetSubscriptionsEndDate(LocalDate.now().plusMonths(1));
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isValid(Subscription subscription) {
        if(subscription.getPlanType().equals(PlanType.FREE)) {
            return true;
        }
        LocalDate endDate = subscription.getGetSubscriptionsEndDate();
        LocalDate currentDate = LocalDate.now();

        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
    }
}
