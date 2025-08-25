package com.dt.find_restaurant.bookMark.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.dt.find_restaurant.pin.domain.Pin;
import com.dt.find_restaurant.security.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class BookMark {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    public static BookMark create(User user, Pin pin) {
        BookMark bookMark = new BookMark();
        bookMark.updateUser(user);
        bookMark.updatePin(pin);
        return bookMark;
    }

    //Helper Methods
    public void updateUser(User user) {
        this.user = user;
        if (!user.getBookMarks().contains(this)) {
            user.getBookMarks().add(this);
        }
    }

    public void deleteFromUser() {
        if (this.user != null) {
            this.user.getBookMarks().remove(this);
            this.user = null;
        }
    }

    public void updatePin(Pin pin) {
        this.pin = pin;
    }
}
