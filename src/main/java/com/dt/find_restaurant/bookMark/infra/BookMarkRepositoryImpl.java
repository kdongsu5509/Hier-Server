package com.dt.find_restaurant.bookMark.infra;

import com.dt.find_restaurant.bookMark.domain.BookMark;
import com.dt.find_restaurant.bookMark.domain.BookMarkRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookMarkRepositoryImpl implements BookMarkRepository {
    private final BookMarkJpaRepository bookMarkJpaRepository;


    @Override
    public UUID saveAndReturnId(BookMark bookMark) {
        return bookMarkJpaRepository.save(bookMark).getId();
    }

    @Override
    public List<BookMark> findByUserEmail(String userEmail) {
        return bookMarkJpaRepository.findByUserEmail(userEmail);
    }

    @Override
    public Optional<BookMark> findById(UUID id) {
        return bookMarkJpaRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        bookMarkJpaRepository.deleteById(id);
    }
}
