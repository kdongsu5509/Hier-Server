package com.dt.find_restaurant.security.domain;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository {

    //CRUD
    UUID save(User user);

    User findById(UUID id);

    User findByEmail(String email);

    void deleteById(UUID id);

    void update(User user);

    Collection<User> findAll();
}
