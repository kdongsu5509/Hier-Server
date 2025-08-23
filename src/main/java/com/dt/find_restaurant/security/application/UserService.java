package com.dt.find_restaurant.security.application;

import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import com.dt.find_restaurant.security.presentation.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void signUp(UserDto req) {
        userRepository.save(toUser(req));
    }

    public void signUpAsAdmin(UserDto req) {

        userRepository.save(toAdmin(req));
        log.info("관리자 등록 성공");
    }

    public boolean isEmailDuplicate(String email) {
        User byEmail = userRepository.findByEmail(email);
        return byEmail != null;
    }

    private User toUser(UserDto req) {
        return User.create(
                req.email(),
                encodePassword(req),
                "USER",
                req.username(),
                req.profileImageUrl() == null ? null : req.profileImageUrl()
        );
    }

    private User toAdmin(UserDto req) {
        return User.create(
                req.email(),
                encodePassword(req),
                "ADMIN",
                req.username(),
                req.profileImageUrl() == null ? null : req.profileImageUrl(),
                true
        );
    }

    private String encodePassword(UserDto req) {
        return passwordEncoder.encode(req.password());
    }
}
