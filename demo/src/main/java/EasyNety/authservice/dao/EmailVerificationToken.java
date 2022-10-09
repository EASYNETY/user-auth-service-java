package EasyNety.authservice.dao;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "email_verification_token")
public class EmailVerificationToken extends BaseEntity {
    @Id
    private String userName;
    private String verificationToken;
    private Boolean used = false;
}
