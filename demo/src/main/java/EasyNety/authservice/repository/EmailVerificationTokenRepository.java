package EasyNety.authservice.repository;

import EasyNety.authservice.dao.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken,String> {

    Optional<EmailVerificationToken> findByVerificationToken(String token);
}

