package com.dt.find_restaurant.user.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCommand {

    @Getter
    public static class Signup {
        private final String email;
        private final String password;
        private final String koreanName;

        private Signup(String email, String password, String koreanName) {
            this.email = email;
            this.password = password;
            this.koreanName = koreanName;
        }

        public static Signup of(String email, String password, String koreanName) {
            return new Signup(email, password, koreanName);
        }
    }

}
