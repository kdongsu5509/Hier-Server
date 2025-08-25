package com.dt.find_restaurant.bookMark.application;

import com.dt.find_restaurant.bookMark.domain.BookMark;
import com.dt.find_restaurant.bookMark.domain.BookMarkRepository;
import com.dt.find_restaurant.bookMark.dto.BookMarkResponseDto;
import com.dt.find_restaurant.global.exception.CustomExceptions.PinException;
import com.dt.find_restaurant.global.exception.CustomExcpMsgs;
import com.dt.find_restaurant.pin.domain.Pin;
import com.dt.find_restaurant.pin.domain.PinRepository;
import com.dt.find_restaurant.security.domain.User;
import com.dt.find_restaurant.security.domain.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookMarkService {
    private final BookMarkRepository bookMarkRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;

    public UUID addBookMark(String userEmail, UUID pinId) {
        Optional<Pin> existPin = isExistPin(pinId);
        if (existPin.isEmpty()) {
            throw new PinException(CustomExcpMsgs.PIN_NOT_FOUND.getMessage());
        }

        User byEmail = userRepository.findByEmail(userEmail);

        BookMark bookMark = BookMark.create(byEmail, existPin.get());
        log.info("북마크 생성: {}", bookMark);

        return bookMarkRepository.saveAndReturnId(bookMark);
        //북마크 테이블에 사용자 아이디, 핀 아이디를 저장.
        //북마크 테이블은 User와 양방향 관계를 가짐.
        //북마크 테이블은 Pin과 양방향 관계를 가짐.

    }

    private Optional<Pin> isExistPin(UUID pinId) {
        return pinRepository.findById(pinId);
    }

    public List<BookMarkResponseDto> getAllMyBookMarks(String userEmail) {
        List<BookMark> byUserEmail = bookMarkRepository.findByUserEmail(userEmail);
        return byUserEmail.stream()
                .map(this::toBookMarkResponse)
                .toList();
    }

    public void deleteBookMark(String userEmail, UUID bookMarkId) {
        //내 북마크가 맞는지 확인
        BookMark bookMark = validateBookMarkDeleteRequest(userEmail, bookMarkId);
        bookMark.deleteFromUser();
        bookMarkRepository.deleteById(bookMark.getId());
        log.info("북마크 삭제: {}", bookMarkId);
    }

    private BookMarkResponseDto toBookMarkResponse(BookMark bookMark) {
        return new BookMarkResponseDto(
                bookMark.getPin().getId(),
                bookMark.getPin().getPlaceName()
        );
    }


    private BookMark validateBookMarkDeleteRequest(String userEmail, UUID bookMarkId) {
        //1. 북마크 존재 여부 확인
        BookMark bookMark = bookMarkRepository.findById(bookMarkId)
                .orElseThrow(() -> new PinException(CustomExcpMsgs.BOOKMARK_NOT_FOUND.getMessage() + bookMarkId));
        //2. 내가 이 북마크를 쓴게 맞는지 확인
        if (!bookMark.getUser().getEmail().equals(userEmail)) {
            throw new PinException(CustomExcpMsgs.BOOKMARK_UNAUTHORIZED.getMessage());
        }
        return bookMark;
    }


}
