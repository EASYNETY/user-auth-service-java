package EasyNety.authservice.controller;

import EasyNety.authservice.exception.AppException;
import EasyNety.authservice.models.*;
import EasyNety.authservice.service.AuthService;
import EasyNety.authservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/local-signUp")
    public UserModel localSignUp(@RequestBody SignUpRequest request)throws AppException {
        return authService.LocalSignUp(request);
    }

    @GetMapping("/verifyMail")
    public MessageResponse verifyToken(@RequestParam(value = "token", required = false) String token ) throws AppException {
        return emailService.verifyMail(token);
    }

    @PostMapping("/local-login")
    public JwtModel localLogin(@RequestBody LoginRequest request) throws Exception {
        return authService.LocalLogin(request);
    }
}
