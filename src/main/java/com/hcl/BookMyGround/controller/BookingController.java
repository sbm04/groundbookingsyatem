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
            Long userId = Long.valueOf(bookingData.get("userId").toString());
            Long groundId = Long.valueOf(bookingData.get("groundId").toString());
            Long timeSlotId = Long.valueOf(bookingData.get("timeSlotId").toString());
            LocalDate bookingDate = LocalDate.parse(bookingData.get("bookingDate").toString());
            String statusInput = bookingData.get("status").toString();
            double totalAmount = Double.parseDouble(bookingData.get("totalAmount").toString());
            String paymentMethod = bookingData.get("paymentMethod").toString();

            BookingStatus status = BookingStatus.valueOf(statusInput.toUpperCase());

            Booking booking = new Booking();
            booking.setBookingDate(bookingDate);
            booking.setStatus(status);
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