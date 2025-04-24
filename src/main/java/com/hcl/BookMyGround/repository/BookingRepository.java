package com.hcl.BookMyGround.repository;


import com.hcl.BookMyGround.enums.BookingStatus;
import com.hcl.BookMyGround.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByGround_GroundIdAndBookingDate(Long groundId, LocalDate bookingDate);

    List<Booking> findByUserUserId(Long userId);

    List<Booking> findByUserUserIdAndBookingDateAfter(Long userId, LocalDate date);

//    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId AND b.bookingDate >= :today AND b.status = :status")
//    List<Booking> findUpcomingConfirmedBookingsByUserId(@Param("userId") Long userId,
//                                                        @Param("today") LocalDate today,
//                                                        @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.user.userId = :userId AND b.bookingDate >= :today AND b.status = com.hcl.BookMyGround.enums.BookingStatus.CONFIRMED")
    List<Booking> findUpcomingConfirmedBookingsByUserId(@Param("userId") Long userId, @Param("today") LocalDate today);


    List<Booking> findByUserUserIdAndBookingDateBefore(Long userId, LocalDate date);

     boolean existsByGround_GroundIdAndBookingDateAndTimeSlot_Id(Long groundId, LocalDate bookingDate, Long timeSlotId);

    @Query("SELECT b.timeSlot.id FROM Booking b WHERE b.ground.groundId = :groundId AND b.bookingDate = :date AND b.status = 'CONFIRMED'")
    List<Long> findBookedTimeSlotIds(@Param("groundId") Long groundId, @Param("date") LocalDate date);


}