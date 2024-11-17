package com.projectmanagement.controllers;

import com.projectmanagement.dto.responses.PaymentLinkResponse;
import com.projectmanagement.models.PlanType;
import com.projectmanagement.models.User;
import com.projectmanagement.services.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String apiKey;

    private final UserService userService;

    public PaymentController(UserService userService) {
        this.userService = userService;
        Stripe.apiKey = apiKey; // Thiết lập khóa API Stripe
    }

    @PostMapping("/{planType}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @PathVariable PlanType planType,
            @RequestHeader("Authorization") String jwt
    ) {
        try {
            User user = userService.findUserProfileByJwt(jwt);

            int amount = 799 * 100;
            if (planType.equals(PlanType.ANNUALLY)) {
                amount *= 12;
                amount = (int) (amount * 0.7);
            }

            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", "INR");
            params.put("customer", user.getEmail()); // Giả định rằng bạn đã có customer trong Stripe

            params.put("callback_url", "http://localhost:5173/update_plan/success?planType=" + planType);

            PaymentLink payment = PaymentLink.create(params);

            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPayment_link_url(payment.getUrl());
            res.setPayment_link_id(payment.getId());

            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
