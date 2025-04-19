package com.hcl.BookMyGround.model;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcl.BookMyGround.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;



@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ground_id", nullable = false)
    private Ground ground;

    private LocalDate bookingDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;
    private double totalAmount;
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private Payment payment;

}