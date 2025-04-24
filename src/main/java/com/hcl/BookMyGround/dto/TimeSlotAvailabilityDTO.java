package com.hcl.BookMyGround.dto;

import lombok.*;

import java.time.LocalTime;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotAvailabilityDTO {
    private Long slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean booked;
}
