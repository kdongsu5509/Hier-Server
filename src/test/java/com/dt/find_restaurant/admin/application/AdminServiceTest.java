package com.dt.find_restaurant.admin.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.dt.find_restaurant.admin.dto.UserInfoResponseDto;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("요청이 들어오면 모든 사용자를 조회한다")
    void findAllUsers_ShouldReturnListOfUserInfoResponseDto() {
        // Given
        User user1 = User.create("test@test.com", "Test User", "ADMIN", "USER", null, true);
        User user2 = User.create("test@test.com", "Test User", "USER", "USER", null, true);
        User user3 = User.create("test@test.com", "Test User", "USER", "USER", null, true);
        User user4 = User.create("test@test.com", "Test User", "USER", "USER", null, true);
        User user5 = User.create("test@test.com", "Test User", "USER", "USER", null, true);
        User user6 = User.create("test@test.com", "Test User", "USER", "USER", null, true);

        List<User> mockUsers = List.of(user1, user2, user3, user4, user5, user6);
        given(userRepository.findAll()).willReturn(mockUsers);

        // When
        List<UserInfoResponseDto> resultsUsers = adminService.findAllUsers();

        // Then
        then(userRepository).should(times(1)).findAll();

        assertThat(resultsUsers.size()).isEqualTo(mockUsers.size());

        for (int i = 0; i < mockUsers.size(); i++) {
            UserInfoResponseDto userInfo = resultsUsers.get(i);
            User mockUser = mockUsers.get(i);
            assertThat(userInfo.uuid()).isEqualTo(mockUser.getUuid());
            assertThat(userInfo.email()).isEqualTo(mockUser.getEmail());
            assertThat(userInfo.username()).isEqualTo(mockUser.getUserName());
            assertThat(userInfo.profileImageUrl()).isEqualTo(mockUser.getProfileImageUrl());
            assertThat(userInfo.role()).isEqualTo(mockUser.getRole());
            assertThat(userInfo.isEnable()).isEqualTo(mockUser.isEnabled());
        }
    }
}