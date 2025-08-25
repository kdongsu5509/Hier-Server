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

    public boolean isEmailUnique(String email) {
        User byEmail = userRepository.findByEmail(email);
        return byEmail == null; // 이메일이 존재하지 않으면 중복되지 않음
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

    public boolean isNameUnique(String name) {
        User byUserName = userRepository.findByUserName(name);
        return byUserName == null; // 이름이 존재하지 않으면 중복되지 않음
    }
}
