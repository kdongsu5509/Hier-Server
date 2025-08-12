package com.dt.find_restaurant.user.service;

import com.dt.find_restaurant.global.util.JsonUtils;
import com.dt.find_restaurant.user.domain.User;
import com.dt.find_restaurant.user.domain.UserCommand.Signup;
import com.dt.find_restaurant.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void saveUser(Signup command) {
        final String encodedPassword = passwordEncoder.encode(command.getPassword());
        final String role = "ROLE_USER";

        isDuplicated(command.getEmail());
        User user = User.create(command.getEmail(), encodedPassword, command.getKoreanName(), role);
        User resSignUp = userRepository.save(user);

        log.info("User signup successful : {}", JsonUtils.toJson(resSignUp));
    }

    private void isDuplicated(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new RuntimeException("User with email " + email + " already exists");
        });
    }
}
