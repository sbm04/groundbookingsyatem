package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.model.Payment;
import com.hcl.BookMyGround.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(Payment payment) {
        // Simulate payment processing logic
        // For example, integrate with a payment gateway here

        // For now, assume payment is always successful
        payment.setPaymentStatus("SUCCESS");
        return paymentRepository.save(payment);
    }
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

}