package com.hcl.BookMyGround.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
}