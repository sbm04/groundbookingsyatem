package com.hcl.BookMyGround.service;

import com.hcl.BookMyGround.dto.TimeSlotDTO;
import com.hcl.BookMyGround.model.Ground;
import com.hcl.BookMyGround.model.TimeSlot;
import com.hcl.BookMyGround.repository.GroundRepository;
import com.hcl.BookMyGround.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TimeSlotService {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private GroundRepository groundRepository;


    public TimeSlot createTimeSlot(TimeSlotDTO timeSlotDTO) {


        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStartTime(timeSlotDTO.getStartTime());
        timeSlot.setEndTime(timeSlotDTO.getEndTime());


        return timeSlotRepository.save(timeSlot);
    }
}
