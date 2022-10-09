package EasyNety.authservice.service;

import EasyNety.authservice.dao.EmailVerificationToken;
import EasyNety.authservice.dao.Users;
import EasyNety.authservice.exception.AppException;
import EasyNety.authservice.models.MessageResponse;
import EasyNety.authservice.repository.EmailVerificationTokenRepository;
import EasyNety.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final String OTP = "EMAIL VERIFICATION ";
    private final JavaMailSender javaMailSender;
    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public boolean sendMail(String userName, String link) {
        try {
            MimeMessage mimeMsg=javaMailSender.createMimeMessage();
            MimeMessageHelper helper =new MimeMessageHelper(mimeMsg, true);

            helper.setFrom("awoyeyetimilehin@gmail.com");
            helper.setTo(userName);
            helper.setSubject(OTP);
            helper.setText(link);
            javaMailSender.send(mimeMsg);

            return true;
        }catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public MessageResponse verifyMail(String token) throws AppException {
        MessageResponse response = new MessageResponse();
        Optional<EmailVerificationToken> emailVerificationToken = tokenRepository.findByVerificationToken(token);
        if (emailVerificationToken.isEmpty()) {


            throw new AppException("TOKEN NOT FOUND", HttpStatus.NOT_FOUND);
        }

        if (emailVerificationToken.get().getUsed()){

            throw new AppException("ACCOUNT ALREADY VERIFIED",HttpStatus.BAD_REQUEST);
        }

        Optional <Users>  user = userRepository.findByUserNameAndDeletedFalse(emailVerificationToken.get().getUserName());
        Users users = user.get();
        users.setEnabled(true);

        userRepository.save(users);
        response.setMessage("Verified Successfully");
        return response;

    }


}
