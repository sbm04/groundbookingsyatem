package com.hcl.BookMyGround.repository;

import com.hcl.BookMyGround.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
