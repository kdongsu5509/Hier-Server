package com.dt.find_restaurant.user.application;

import com.dt.find_restaurant.global.exception.CustomExceptions.UserException;
import com.dt.find_restaurant.global.exception.CustomExcpMsgs;
import com.dt.find_restaurant.security.domain.UserRepository;
import com.dt.find_restaurant.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyService {
    private final UserRepository userRepository;

    public UserInfoResponseDto getMyInfo(String userEmail) {
        log.info("내 정보 서비스 - 내 정보 조회 요청: userEmail={}", userEmail);
        return userRepository.findByEmail(userEmail)
                .map(user -> new UserInfoResponseDto(
                                user.getEmail(),
                                user.getUserName(),
                                user.getProfileImageUrl(),
                                user.getRole()
                        )
                ).orElseThrow(() -> new UserException(CustomExcpMsgs.USER_NOT_FOUND.getMessage()));
    }
}
