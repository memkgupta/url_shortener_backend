package com.url_shortner.user_service.services.user;

import com.url_shortner.user_service.dtos.UserUpdateRequest;
import com.url_shortner.user_service.entities.User;
import com.url_shortner.user_service.repositories.UserRepository;
import com.url_shortner.user_service.services.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User create(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    public User updateUser(UserUpdateRequest request) {
        User user = authService.getCurrentUser();
        user.setName(request.getName());
        user.setProfile(request.getProfile());
        return userRepository.save(user);
    }
}
