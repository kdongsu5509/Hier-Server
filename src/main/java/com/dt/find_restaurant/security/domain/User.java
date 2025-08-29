package com.dt.find_restaurant.security.domain;

import static lombok.AccessLevel.PROTECTED;

import com.dt.find_restaurant.bookMark.domain.BookMark;
import com.dt.find_restaurant.global.domain.BaseEntity;
import com.dt.find_restaurant.pin.domain.Pin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = "email")
})
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;
    @Column(unique = true, nullable = false)
    private String userName;
    private String profileImageUrl;
    private boolean enabled;


    @OneToMany(mappedBy = "user")
    private List<Pin> myPins = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BookMark> bookMarks = new ArrayList<>();

    private User(String email, String password, String role, String userName, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.enabled = false; // Default value for enabled
    }

    private User(String email, String password, String role, String userName, String profileImageUrl, boolean enabled) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.enabled = enabled; // Set enabled based on the parameter
    }

    public static User create(String email, String password, String role, String userName) {
        return new User(email, password, role, userName, null);
    }

    public static User create(String email, String password, String role, String userName, String profileImageUrl) {
        return new User(email, password, role, userName, profileImageUrl);
    }

    public static User create(String email, String password, String role, String userName, String profileImageUrl,
                              boolean enabled) {
        return new User(email, password, role, userName, profileImageUrl, enabled);
    }

    public void enableUser() {
        this.enabled = true;
    }

}
