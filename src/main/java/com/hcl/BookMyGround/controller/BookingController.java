package com.hcl.BookMyGround.controller;

import com.hcl.BookMyGround.dto.BookingDTO;
import com.hcl.BookMyGround.enums.BookingStatus;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.Payment;
import com.hcl.BookMyGround.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
            // Retrieve and validate each field
            Long userId = bookingData.containsKey("userId") ? Long.valueOf(bookingData.get("userId").toString()) : null;
            Long groundId = bookingData.containsKey("groundId") ? Long.valueOf(bookingData.get("groundId").toString()) : null;
            Long timeSlotId = bookingData.containsKey("slotId") ? Long.valueOf(bookingData.get("slotId").toString()) : null;
            LocalDate bookingDate = bookingData.containsKey("bookingDate") ? LocalDate.parse(bookingData.get("bookingDate").toString()) : null;
            Double totalAmount = bookingData.containsKey("totalAmount") ? Double.parseDouble(bookingData.get("totalAmount").toString()) : null;
            String paymentMethod = bookingData.containsKey("paymentMethod") ? bookingData.get("paymentMethod").toString() : null;

            // Check for null values and throw an error if any required field is missing
            if (userId == null || groundId == null || timeSlotId == null || bookingDate == null || totalAmount == null || paymentMethod == null) {
                return ResponseEntity.badRequest().body("Missing required fields in the booking data.");
            }

            Booking booking = new Booking();
            booking.setBookingDate(bookingDate);
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.setTotalAmount(totalAmount);

            Payment payment = new Payment();
            payment.setAmount(totalAmount);
            payment.setPaymentDate(bookingDate);
            payment.setPaymentMethod(paymentMethod);

            BookingDTO newBooking = bookingService.createBooking(userId, groundId, timeSlotId, booking, payment);
            return ResponseEntity.ok(newBooking);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }

    @PostMapping("/cancel/{bookingId}/user/{userId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId, @PathVariable Long userId) {
        String response = bookingService.cancelBooking(bookingId, userId);
        return ResponseEntity.ok(response);
    }






}