package com.dt.find_restaurant.user.domain;

import com.dt.find_restaurant.global.util.BaseTimeEntity;
import com.dt.find_restaurant.post.repository.PostEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "email", length = 99, unique = true, nullable = false)
    @Getter
    private String email;

    @Column(name = "password", length = 99, nullable = false)
    private String password;

    @Column(name = "korean_name", length = 20, nullable = false)
    private String koreanName;

    @Setter
    @Column(name = "profile_image_url", length = 255, nullable = true)
    private String profileImageUrl;

    @Column(name = "role", length = 20, nullable = false)
    private String role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<PostEntity> posts = new ArrayList<>();

    private User(String email, String password, String koreanName, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.koreanName = koreanName;
    }

    public static User create(String email, String password, String koreaname, String role) {
        return new User(email, password, koreaname, role);
    }
}
