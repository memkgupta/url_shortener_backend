package com.url_shortner.user_service.repositories;

import com.url_shortner.user_service.entities.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth,Long> {
    public Auth findByEmail(String email);
}
