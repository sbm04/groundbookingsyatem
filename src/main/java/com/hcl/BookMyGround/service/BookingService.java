package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.dto.BookingDTO;
import com.hcl.BookMyGround.dto.UserDTO;
import com.hcl.BookMyGround.enums.BookingStatus;
import com.hcl.BookMyGround.exception.ResourceNotFoundException;
import com.hcl.BookMyGround.dto.PaymentDTO;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.*;
import com.hcl.BookMyGround.repository.BookingRepository;
import com.hcl.BookMyGround.repository.GroundRepository;
import com.hcl.BookMyGround.repository.TimeSlotRepository;
import com.hcl.BookMyGround.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private GroundRepository groundRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository; // ✅ New

    @Autowired
    private PaymentService paymentService;

    public BookingDTO createBooking(Long userId, Long groundId, Long timeSlotId, Booking booking, Payment payment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Ground ground = groundRepository.findById(groundId)
                .orElseThrow(() -> new IllegalArgumentException("Ground not found with ID: " + groundId));
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new IllegalArgumentException("TimeSlot not found with ID: " + timeSlotId));

        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Booking date cannot be in the past.");
        }

        boolean isBooked = bookingRepository.existsByGround_GroundIdAndBookingDateAndTimeSlot_Id(
                groundId, booking.getBookingDate(), timeSlotId);
        if (isBooked) {
            throw new IllegalArgumentException("This time slot is already booked for the selected ground and date.");
        }

        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentStatus("PENDING");
        Payment processedPayment = paymentService.processPayment(payment);

        if (!"SUCCESS".equalsIgnoreCase(processedPayment.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment failed. Booking cannot be completed.");
        }

        booking.setUser(user);
        booking.setGround(ground);
        booking.setTimeSlot(timeSlot); // ✅ Set time slot
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPayment(processedPayment);

        Booking savedBooking = bookingRepository.save(booking);

        processedPayment.setBooking(savedBooking);
        paymentService.updatePayment(processedPayment);
        return mapToDTO(savedBooking);
    }

    public String cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized cancellation attempt");
        }

        if (booking.getBookingDate().atStartOfDay().isBefore(LocalDateTime.now().plusHours(12))) {
            throw new RuntimeException("Booking can only be cancelled at least 12 hours in advance.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        double refundedAmount = booking.getTotalAmount();
        booking.setTotalAmount(0);

        bookingRepository.save(booking);
        return "Booking cancelled. ₹" + refundedAmount + " will be refunded to your account.";
    }

    private BookingDTO mapToDTO(Booking booking) {
        User user = booking.getUser();
        UserDTO userDTO = new UserDTO(user.getUserId(), user.getName(), user.getContactNumber(), user.getEmail());

        Payment payment = booking.getPayment();
        PaymentDTO paymentDTO = null;
        if (payment != null) {
            paymentDTO = new PaymentDTO();
            paymentDTO.setPaymentId(payment.getPaymentId());
            paymentDTO.setPaymentStatus(payment.getPaymentStatus());
            paymentDTO.setPaymentDate(payment.getPaymentDate());
            paymentDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentDTO.setAmount(payment.getAmount());
        }

        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setUser(userDTO);
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(String.valueOf(booking.getStatus()));
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setPayment(paymentDTO);

        return dto;
    }
}