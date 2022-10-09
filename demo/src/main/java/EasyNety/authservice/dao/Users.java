package EasyNety.authservice.dao;

import EasyNety.authservice.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseEntity implements UserDetails, ToModel  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Enumerated(EnumType.STRING)
//    private Provider provider;

    private String userName;

    private String password;

    private Boolean enabled;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String firstName;
    private String lastName;
    private Boolean deleted;


    @Override
    public UserModel toModel() {
        return UserModel.builder()
                .userName(this.userName)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = getRole();
        if (role != null) {
            String roleName = "ROLE_" + role.getName();
            authorities.add(new SimpleGrantedAuthority(roleName));
        }

//        emptyIfNullStream(this.authorities)
//                .map(a -> a.getAuthority().toUpperCase())
//                .map(SimpleGrantedAuthority::new)
//                .forEach(authorities::add);

        return authorities;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    public static <T> Stream<T> emptyIfNullStream(Collection<T> list) {
        return (list == null) ? Stream.empty() : list.stream();
    }
}

