package com.dt.find_restaurant.bookMark.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarkRepository {
    UUID saveAndReturnId(BookMark bookMark);

    List<BookMark> findByUserEmail(String userEmail);

    Optional<BookMark> findById(UUID id);

    void deleteById(UUID id);
}
