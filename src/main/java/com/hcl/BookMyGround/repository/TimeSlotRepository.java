package com.hcl.BookMyGround.repository;


import com.hcl.BookMyGround.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // Add custom methods here if needed in future
}