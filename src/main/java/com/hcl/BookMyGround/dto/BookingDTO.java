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
public class BookingDTO {
    private Long bookingId;
    private UserDTO user;
    private LocalDate bookingDate;
    private String status;
    private double totalAmount;
}
