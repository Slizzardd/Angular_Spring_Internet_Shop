package ua.com.alevel.persistence.repository;

import org.springframework.stereotype.Repository;
import ua.com.alevel.persistence.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User>{
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);


}
