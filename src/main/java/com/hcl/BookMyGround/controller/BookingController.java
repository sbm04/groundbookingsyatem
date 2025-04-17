package com.hcl.BookMyGround.controller;

import com.hcl.BookMyGround.dto.BookingDTO;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> bookingData) {
        try {
            Long userId = Long.valueOf(bookingData.get("userId").toString());
            Long groundId = Long.valueOf(bookingData.get("groundId").toString());

            Booking booking = new Booking();
            booking.setBookingDate(LocalDate.parse((String) bookingData.get("bookingDate")));
            booking.setStatus((String) bookingData.get("status"));
            booking.setTotalAmount(Double.parseDouble(bookingData.get("totalAmount").toString()));

            BookingDTO newBooking = bookingService.createBooking(userId, groundId, booking);
            return ResponseEntity.ok(newBooking);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating the booking.");
        }
    }



}