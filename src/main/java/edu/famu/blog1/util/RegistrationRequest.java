package edu.famu.blog1.util;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class RegistrationRequest {
    private String email;
    private String password;
    private @Nullable String displayName;
}
