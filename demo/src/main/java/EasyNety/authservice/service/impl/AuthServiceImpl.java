package EasyNety.authservice.service.impl;

import EasyNety.authservice.dao.Users;
import EasyNety.authservice.exception.AppException;
import EasyNety.authservice.models.JwtModel;
import EasyNety.authservice.models.LoginRequest;
import EasyNety.authservice.models.SignUpRequest;
import EasyNety.authservice.models.UserModel;
import EasyNety.authservice.repository.UserRepository;
import EasyNety.authservice.service.AuthService;
import EasyNety.authservice.service.TokenAuthenticationService;
import EasyNety.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TokenAuthenticationService tokenService;
    @Override
    public UserModel LocalSignUp(SignUpRequest request) throws AppException {
        System.out.println("here");
        if (userRepository.existsByUserName(request.getEmail())){
            throw new AppException("Account with Email Already Exists", HttpStatus.BAD_REQUEST);
        }
        Users user = new Users();
        user.setUserName(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDeleted(false);
        userRepository.save(user);

        userService.sendverifyMail(request.getEmail());
        return user.toModel();
    }

    @Override
    public JwtModel LocalLogin(LoginRequest request) throws Exception {
        Optional <Users> userFound = userRepository.findByUserNameAndDeletedFalse(request.getEmail());
        if (!userFound.isPresent()){
            throw new AppException("User does not exist",HttpStatus.BAD_REQUEST);
        }
        Users user = userFound.get();
        System.out.println(user.getEnabled());
        if (user.getEnabled() == false){
            throw new AppException("Account not enabled, verify account via mail",HttpStatus.BAD_REQUEST);
        }
        matchPassword(request.getPassword(), user.getPassword(),user);
        JwtModel response =  tokenService.generatorToken(user);
        return response;
    }

    private void matchPassword(String plainPassword, String encryptedPassword,Users user) throws AppException {
        if (!passwordEncoder.matches(plainPassword, encryptedPassword)) {
            userRepository.save(user);
            System.out.println(user);
            throw new AppException("Invalid Credentials",HttpStatus.BAD_REQUEST);
        }
    }



}

