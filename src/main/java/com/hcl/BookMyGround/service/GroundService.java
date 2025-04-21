package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.model.Ground;
import com.hcl.BookMyGround.repository.GroundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroundService {
    @Autowired
    private GroundRepository groundRepository;

    public Ground addGround(Ground ground) {
        Ground savedGround = groundRepository.save(ground);
//        savedGround.createTimeSlots(); // Automatically create time slots
        return savedGround;
    }

    public List<Ground> getAllGrounds() {
        return groundRepository.findAll();
    }
}