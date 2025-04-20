package com.hcl.BookMyGround.controller;


import com.hcl.BookMyGround.dto.TimeSlotDTO;
import com.hcl.BookMyGround.model.TimeSlot;
import com.hcl.BookMyGround.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController {

    @Autowired
    private TimeSlotService timeSlotService;

    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO) {
        TimeSlot created = timeSlotService.createTimeSlot(timeSlotDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

}
