package com.dt.find_restaurant.security.application;

import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import com.dt.find_restaurant.security.presentation.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void join(UserDto req) {
        userRepository.save(toUser(req));
    }

    private User toUser(UserDto req) {
        return User.create(
                req.email(),
                encodePassword(req),
                "USER",
                req.userName(),
                req.profileImageUrl() == null ? null : req.profileImageUrl()
        );
    }

    private String encodePassword(UserDto req) {
        return passwordEncoder.encode(req.password());
    }
}
