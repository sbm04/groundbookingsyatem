package com.hcl.BookMyGround.controller;

import com.hcl.BookMyGround.dto.BookingDTO;
import com.hcl.BookMyGround.dto.UserDTO;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.User;
import com.hcl.BookMyGround.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Anyone can register
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
//        // Check if user already exists
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("error", "User with email already exists.");
//            errorResponse.put("email", user.getEmail());
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
//        }
        UserDTO userDTO = userService.register(user);
        return ResponseEntity.ok(userDTO);
    }

    // ✅ Only authenticated user with role USER can fetch their own bookings
    @GetMapping("/{userId}/bookings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Booking>> getAllBookings(
            @PathVariable Long userId,
            Authentication authentication) {

        // Extract authenticated user's ID from principal
        Long loggedInUserId = Long.parseLong(authentication.getName()); // assuming userId stored as username

        if (!loggedInUserId.equals(userId)) {
            return ResponseEntity.status(403).build(); // forbidden
        }

        List<Booking> bookings = userService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }
}
