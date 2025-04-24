package com.hcl.BookMyGround.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String name;
    private String contactNumber;
    private String email;

}
