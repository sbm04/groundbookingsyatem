package com.hcl.BookMyGround.controller;

import com.hcl.BookMyGround.dto.UserDTO;
import com.hcl.BookMyGround.model.Booking;
import com.hcl.BookMyGround.model.User;
import com.hcl.BookMyGround.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        UserDTO userDTO = userService.register(user);
        return ResponseEntity.ok(userDTO);
    }
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> credentials) {
        return userService.login(credentials.get("email"), credentials.get("password"))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    //user can abe to see past booking and upcomming booking

    @GetMapping("/{userId}/bookings")
    public ResponseEntity<List<Booking>> getAllBookings(@PathVariable Long userId) {
        List<Booking> bookings = userService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{userId}/bookings/upcoming")
    public ResponseEntity<List<Booking>> getUpcomingBookings(@PathVariable Long userId) {
        List<Booking> upcoming = userService.getUpcomingBookings(userId);
        return ResponseEntity.ok(upcoming);
    }

    @GetMapping("/{userId}/bookings/past")
    public ResponseEntity<List<Booking>> getPastBookings(@PathVariable Long userId) {
        List<Booking> past = userService.getPastBookings(userId);
        return ResponseEntity.ok(past);
    }

}