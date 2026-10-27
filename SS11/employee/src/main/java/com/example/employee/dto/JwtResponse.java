package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private String username;
}
