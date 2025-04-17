package com.hcl.BookMyGround.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long userId;
    private Long groundId;
    private LocalDate bookingDate;
    private double totalAmount;
    private String paymentMethod;
}
