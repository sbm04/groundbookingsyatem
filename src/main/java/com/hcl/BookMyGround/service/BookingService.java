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
import com.hcl.BookMyGround.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private PaymentService paymentService;

    public BookingDTO createBooking(Long userId, Long groundId, Booking booking, Payment payment) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Ground ground = groundRepository.findById(groundId)
                .orElseThrow(() -> new IllegalArgumentException("Ground not found with ID: " + groundId));



        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Booking date cannot be in the past.");
        }


        boolean isBooked = bookingRepository.existsByGround_GroundIdAndBookingDate(groundId, booking.getBookingDate());
        if (isBooked) {
            throw new IllegalArgumentException("The ground is already booked for the selected date.");
        }



        payment.setAmount(booking.getTotalAmount());
        payment.setPaymentStatus("PENDING");
        Payment processedPayment = paymentService.processPayment(payment);

        if (!"SUCCESS".equalsIgnoreCase(processedPayment.getPaymentStatus())) {
            throw new IllegalArgumentException("Payment failed. Booking cannot be completed.");
        }


        booking.setUser(user);
        booking.setGround(ground);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPayment(processedPayment);


        Booking savedBooking = bookingRepository.save(booking);


        processedPayment.setBooking(savedBooking);
        paymentService.updatePayment(processedPayment);
        return mapToDTO(savedBooking);
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