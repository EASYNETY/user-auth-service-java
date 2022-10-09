package EasyNety.authservice.service;

import EasyNety.authservice.exception.AppException;
import EasyNety.authservice.models.JwtModel;
import EasyNety.authservice.models.LoginRequest;
import EasyNety.authservice.models.SignUpRequest;
import EasyNety.authservice.models.UserModel;

public interface AuthService {


    UserModel LocalSignUp(SignUpRequest request) throws AppException;


    JwtModel LocalLogin(LoginRequest request) throws Exception;
}

