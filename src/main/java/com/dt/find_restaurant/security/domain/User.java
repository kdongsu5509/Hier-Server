package com.dt.find_restaurant.security.domain;

import static lombok.AccessLevel.PROTECTED;

import com.dt.find_restaurant.global.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uuid;

    private String email;
    private String password;
    private String role;
    private String userName;
    private String profileImageUrl;
    private boolean enabled;

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

}
