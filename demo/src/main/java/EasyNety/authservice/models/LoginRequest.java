package EasyNety.authservice.models;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}

