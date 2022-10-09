package EasyNety.authservice.service;

import EasyNety.authservice.dao.TokenStore;
import EasyNety.authservice.dao.Users;
import EasyNety.authservice.models.JwtModel;
import EasyNety.authservice.models.UserAuthentication;
import EasyNety.authservice.repository.TokenStoreRepository;
import EasyNety.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static java.util.Optional.empty;
@Service
public class TokenAuthenticationService {


    public static final String AUTH_HEADER_NAME = "AUTHORIZATION";
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;
    private final TokenStoreRepository tokenStoreRepository;
    private final Long expiration;

    @Autowired
    public TokenAuthenticationService(JwtGenerator jwtGenerator,
                                      UserRepository userRepository,
                                      TokenStoreRepository tokenStoreRepository,
                                      @Value("${token.expiration}") Long expiration) {
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
        this.tokenStoreRepository = tokenStoreRepository;
        this.expiration = expiration;
    }

    public JwtModel generatorToken(Users user) {
        final String token = jwtGenerator.generateToken(user);
        LocalDateTime expires = getExpiryDate();
        tokenStoreRepository.save(new TokenStore(token, expires));
        JwtModel jwt = new JwtModel(token,expires);
        return jwt;
    }

    private LocalDateTime getExpiryDate() {
        return JwtGenerator.generateExpirationDate(expiration)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Optional<UserAuthentication> getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null && tokenStoreRepository.findByToken(token).isPresent()) {
            String username = jwtGenerator.getUsernameFromToken(token);
            Users user = userRepository.findByUserNameAndDeletedFalse(username).orElse(null);
            if (user != null) {
                return Optional.of(new UserAuthentication(user));
            }
        }
        return empty();
    }
}
