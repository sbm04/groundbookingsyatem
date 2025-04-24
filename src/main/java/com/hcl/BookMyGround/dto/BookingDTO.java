package com.hcl.BookMyGround.dto;

import com.hcl.BookMyGround.enums.BookingStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long bookingId;
    private UserDTO user;
    private LocalDate bookingDate;
    private BookingStatus status;
    private double totalAmount;
    private GroundDTO ground;
    private TimeSlotDTO timeSlot;
    private PaymentDTO payment;
}
