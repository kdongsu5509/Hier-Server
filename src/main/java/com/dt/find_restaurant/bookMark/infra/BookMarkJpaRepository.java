package com.dt.find_restaurant.bookMark.infra;

import com.dt.find_restaurant.bookMark.domain.BookMark;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookMarkJpaRepository extends JpaRepository<BookMark, UUID> {
    @Query("SELECT b FROM BookMark b JOIN FETCH b.user WHERE b.user.email = :email")
    List<BookMark> findByUserEmail(String email);
}
