package com.hcl.BookMyGround.controller;

import com.hcl.BookMyGround.model.Ground;
import com.hcl.BookMyGround.service.GroundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grounds")
public class GroundController {
    @Autowired
    private GroundService groundService;

    @PostMapping
    public ResponseEntity<Ground> addGround(@RequestBody Ground ground) {
        Ground newGround = groundService.addGround(ground);
        return ResponseEntity.ok(newGround);
    }

    @GetMapping
    public List<Ground> getAllGrounds() {
        return groundService.getAllGrounds();
    }
    // find ground city wise and type cricket


}
