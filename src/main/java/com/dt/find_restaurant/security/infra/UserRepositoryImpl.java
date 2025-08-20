package com.dt.find_restaurant.security.infra;

import static com.dt.find_restaurant.global.exception.CustomExcpMsgs.USER_NOT_FOUND;

import com.dt.find_restaurant.global.exception.CustomExceptions.UserException;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository UserJpaRepository;


    @Override
    public UUID save(User user) {
        return UserJpaRepository.save(user).getUuid();
    }

    @Override
    public User findById(UUID id) {
        return UserJpaRepository.findById(id)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND.getMessage() + id));
    }

    @Override
    public User findByEmail(String email) {
        return UserJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND.getMessage() + email));
    }

    @Override
    public void deleteById(UUID id) {
        if (!UserJpaRepository.existsById(id)) {
            throw new UserException(USER_NOT_FOUND.getMessage() + id);
        }
        UserJpaRepository.deleteById(id);
    }

    @Override
    public void update(User user) {
        if (!UserJpaRepository.existsById(user.getUuid())) {
            throw new UserException(USER_NOT_FOUND.getMessage() + user.getUuid());
        }
        UserJpaRepository.save(user);
    }

    @Override
    public Collection<User> findAll() {
        return UserJpaRepository.findAll();
    }
}
