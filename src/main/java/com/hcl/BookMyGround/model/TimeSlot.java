package com.hcl.BookMyGround.model;

import java.time.LocalTime;
import java.util.*;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;



@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToMany
    @JoinTable(
            name = "ground_timeslot",
            joinColumns = @JoinColumn(name = "timeslot_id"),
            inverseJoinColumns = @JoinColumn(name = "ground_id")
    )
    private List<Ground> availableGrounds;
}
