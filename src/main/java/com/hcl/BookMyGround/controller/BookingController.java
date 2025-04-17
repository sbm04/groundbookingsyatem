package com.hcl.BookMyGround.controller;

import com.hcl.BookMyGround.dto.BookingDTO;
import com.hcl.BookMyGround.enums.BookingStatus;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.Payment;
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
            LocalDate bookingDate = LocalDate.parse(bookingData.get("bookingDate").toString());
            String statusInput = bookingData.get("status").toString().toUpperCase();
            double totalAmount = Double.parseDouble(bookingData.get("totalAmount").toString());
            String paymentMethod = bookingData.get("paymentMethod").toString();

            // ✅ Validate booking status
            BookingStatus status;
            try {
                status = BookingStatus.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid booking status: " + statusInput);
            }

            // ✅ Create Booking object
            Booking booking = new Booking();
            booking.setBookingDate(bookingDate);
            booking.setStatus(status); // store as String
            booking.setTotalAmount(totalAmount);

            // ✅ Create Payment object
            Payment payment = new Payment();
            payment.setAmount(totalAmount);
            payment.setPaymentMethod(paymentMethod);

            // ✅ Call service to create booking
            BookingDTO newBooking = bookingService.createBooking(userId, groundId, booking, payment);
            return ResponseEntity.ok(newBooking);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }

}