package com.hcl.BookMyGround.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Booking booking;

    private double amount;
    private LocalDateTime refundDate;
}
