package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.dto.BookingDTO;
import com.hcl.BookMyGround.dto.UserDTO;
import com.hcl.BookMyGround.exception.ResourceNotFoundException;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.*;
import com.hcl.BookMyGround.repository.BookingRepository;
import com.hcl.BookMyGround.repository.GroundRepository;
import com.hcl.BookMyGround.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroundRepository groundRepository;

    public BookingDTO createBooking(Long userId, Long groundId, Booking booking) {
        // Check for past date
        if (booking.getBookingDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Booking date cannot be in the past.");
        }

        // Check if booking already exists for the ground on the given date
        boolean exists = bookingRepository.existsByGroundGroundIdAndBookingDate(groundId, booking.getBookingDate());
        if (exists) {
            throw new IllegalArgumentException("The ground is already booked for this date.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Ground ground = groundRepository.findById(groundId)
                .orElseThrow(() -> new ResourceNotFoundException("Ground not found"));

        booking.setUser(user);
        booking.setGround(ground);
        Booking savedBooking = bookingRepository.save(booking);

        // Convert to DTO
        UserDTO userDTO = new UserDTO(user.getUserId(), user.getName(), user.getContactNumber(), user.getEmail());
        return new BookingDTO(savedBooking.getBookingId(), userDTO, savedBooking.getBookingDate(), savedBooking.getStatus(), savedBooking.getTotalAmount());
    }
}