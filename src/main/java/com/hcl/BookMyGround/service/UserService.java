package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.dto.UserDTO;
import com.hcl.BookMyGround.model.User;
import com.hcl.BookMyGround.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserDTO register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return new UserDTO(
                savedUser.getUserId(),
                savedUser.getName(),
                savedUser.getContactNumber(),
                savedUser.getEmail()
        );
    }

    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> encoder.matches(password, user.getPassword())); // Password check
    }
}
