package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.dto.UserDTO;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.User;
import com.hcl.BookMyGround.repository.BookingRepository;
import com.hcl.BookMyGround.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BookingRepository bookingRepository;

    public UserDTO register(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Return DTO after registration
        return new UserDTO(
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getContactNumber(),
                savedUser.getEmail()
        );
    }

    public Optional<User> login(String email, String password) {
        // Find user by email and match encoded password
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserUserId(userId);
    }

    public List<Booking> getUpcomingBookings(Long userId) {
        return bookingRepository.findByUserUserIdAndBookingDateAfter(userId, LocalDate.now());
    }

    public List<Booking> getPastBookings(Long userId) {
        return bookingRepository.findByUserUserIdAndBookingDateBefore(userId, LocalDate.now());
    }

}
