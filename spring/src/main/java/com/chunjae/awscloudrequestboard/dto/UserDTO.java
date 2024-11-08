package com.chunjae.awscloudrequestboard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long uid;
    private String ip;
    private String name;
    private String role;
}
