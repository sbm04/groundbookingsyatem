package com.hcl.BookMyGround.repository;

import com.hcl.BookMyGround.model.Ground;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface GroundRepository extends JpaRepository<Ground, Long> {
}
