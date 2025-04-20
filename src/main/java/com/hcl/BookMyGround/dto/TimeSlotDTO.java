package com.hcl.BookMyGround.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class TimeSlotDTO {
    private LocalTime startTime;
    private LocalTime endTime;
    private List<Long> groundIds;
}