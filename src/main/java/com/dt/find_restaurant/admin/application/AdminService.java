package com.dt.find_restaurant.admin.application;

import com.dt.find_restaurant.admin.dto.UserInfoResponseDto;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<UserInfoResponseDto> findAllUsers() {
        log.info("관리자 서비스 - 모든 사용자 정보 조회 요청");
        return userRepository.findAll().stream()
                .map(user -> new UserInfoResponseDto(
                        user.getId(),
                        user.getEmail(),
                        user.getUserName(),
                        user.getProfileImageUrl(),
                        user.getRole(),
                        user.isEnabled())
                ).toList();
    }

    public void unlockUserAccount(UUID userId) {
        log.info("관리자 서비스 - 사용자 계정 잠금 해제 요청: userId={}", userId);
        User byId = userRepository.findById(userId);
        byId.enableUser();
    }
}
