package com.hcl.BookMyGround.controller;

import com.hcl.BookMyGround.model.Ground;
import com.hcl.BookMyGround.security.JwtHelper;
import com.hcl.BookMyGround.service.GroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grounds")
public class GroundController {
    @Autowired
    private GroundService groundService;

    // Add ground - only accessible to admin users
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Ground> addGround(@RequestBody Ground ground, Authentication authentication) {
        // Check if the authenticated user is an admin
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        System.out.println("Roles: " + userDetails.getAuthorities()); // Debug log

        if (userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).build(); // Forbidden if not an admin
        }

        Ground newGround = groundService.addGround(ground);
        return ResponseEntity.ok(newGround);
    }


    @GetMapping
    public List<Ground> getAllGrounds() {
        return groundService.getAllGrounds();
    }
    // find ground city wise and type cricket

    @GetMapping("/search")
    public ResponseEntity<List<Ground>> searchGroundsByCityAndType(
            @RequestParam String city,
            @RequestParam String type) {
        List<Ground> filtered = groundService.findGroundsByLocationAndType(city, type);
        return ResponseEntity.ok(filtered);
    }

}
