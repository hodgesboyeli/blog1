package edu.famu.blog1.util;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
