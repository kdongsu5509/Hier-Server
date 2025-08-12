package com.dt.find_restaurant.security.jwt.domain;

import jakarta.servlet.http.Cookie;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtResult {

    @Getter
    public static class Issue {
        private final String accessToken;
        private final String refreshToken;
        private final Cookie cookie;

        private Issue(String accessToken, String refreshToken, Cookie cookie) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.cookie = cookie;
        }

        public static Issue of(String accessToken, String refreshToken, Cookie cookie) {
            return new Issue(accessToken, refreshToken, cookie);
        }
    }

}
