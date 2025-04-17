package com.hcl.BookMyGround.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroundDTO {
    private Long groundId;
    private String name;
    private String type;
    private String location;
    private double weekdayRate;
    private double weekendRate;
    private String contactPerson;
    private String contactNumber;
}

