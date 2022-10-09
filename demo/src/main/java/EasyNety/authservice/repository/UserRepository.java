package EasyNety.authservice.repository;

import EasyNety.authservice.dao.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long> {
    Users findByUserName(String username);
    Boolean existsByUserName(String username);
    Optional <Users> findByUserNameAndDeletedFalse(String userName);
    Optional <Users> findByUserNameAndDeletedFalseAndEnabledFalse(String userName);
}

