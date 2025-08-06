package com.url_shortner.user_service.repositories;

import com.url_shortner.user_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
}
