package EasyNety.authservice.service.impl;

import EasyNety.authservice.dao.EmailVerificationToken;
import EasyNety.authservice.dao.Users;
import EasyNety.authservice.exception.AppException;
import EasyNety.authservice.repository.EmailVerificationTokenRepository;
import EasyNety.authservice.repository.UserRepository;
import EasyNety.authservice.service.EmailService;
import EasyNety.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    @Override
    public void sendverifyMail(String userName ) throws AppException {
//     System.out.println(userName);
        String url = "localhost:1942/v1/auth/verifyMail?token=";
        Optional<Users> list = userRepository.findByUserNameAndDeletedFalseAndEnabledFalse(userName);
        if (list.isPresent()){
            String token = generateRandomString(16);
            url = url.concat(token);
            EmailVerificationToken entity = new EmailVerificationToken(userName,token,false);
            tokenRepository.save(entity);
            emailService.sendMail(userName,url);

        }else {throw new AppException("User already verified", HttpStatus.CONFLICT);}

    }

//public boolean userExistsAndNotEnabled(String userName){
//    System.out.println(userName);
//    boolean state=false;
//        if (list.isPresent()){
//        System.out.println(list.get().getUsername().concat( "username"));
//        state =true;
//    }
//
//    return state;
//}


    public static String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        String pattern = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";

        StringBuilder sb = new StringBuilder();

        while (length > 0) {
            length--;
            sb.append(pattern.charAt(random.nextInt(pattern.length())));
        }

        return sb.toString();
    }
}
