package com.hcl.BookMyGround.repository;


import com.hcl.BookMyGround.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByGround_GroundIdAndBookingDate(Long groundId, LocalDate bookingDate);

    List<Booking> findByUserUserId(Long userId);

    List<Booking> findByUserUserIdAndBookingDateAfter(Long userId, LocalDate date);

    List<Booking> findByUserUserIdAndBookingDateBefore(Long userId, LocalDate date);

//    boolean existsByGround_GroundIdAndBookingDateAndTimeSlot_TimeSlotId(Long groundId, LocalDate bookingDate, Long timeSlotId);
boolean existsByGround_GroundIdAndBookingDateAndTimeSlot_Id(Long groundId, LocalDate bookingDate, Long timeSlotId);

}