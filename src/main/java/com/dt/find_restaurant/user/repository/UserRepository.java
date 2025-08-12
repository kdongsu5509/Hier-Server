package com.dt.find_restaurant.user.repository;

import com.dt.find_restaurant.user.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    User save(User user);
}
