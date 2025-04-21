package com.hcl.BookMyGround.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ground {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groundId;
    private String name;
    private String type;
    private String location;
    private double weekdayRate;
    private double weekendRate;
    private String contactPerson;
    private String contactNumber;

    @ManyToMany(mappedBy = "availableGrounds", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();


}