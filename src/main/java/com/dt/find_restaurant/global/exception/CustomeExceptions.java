package com.dt.find_restaurant.global.exception;

public class CustomeExceptions {
    public static class PinException extends RuntimeException {
        public PinException(String message) {
            super(message);
        }
    }

    public static class CommentException extends RuntimeException {
        public CommentException(String message) {
            super(message);
        }
    }

    public static class UserException extends RuntimeException {
        public UserException(String message) {
            super(message);
        }
    }
}
