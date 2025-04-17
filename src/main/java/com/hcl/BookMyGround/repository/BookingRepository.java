package com.hcl.BookMyGround.repository;


import com.hcl.BookMyGround.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByGroundGroundIdAndBookingDate(Long groundId, LocalDate bookingDate);
}