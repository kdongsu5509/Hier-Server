package com.dt.find_restaurant.global.exception;

public enum CustomExcpMsgs {
    //    PIN_NOT_FOUND("Pin not found"),
//    PIN_ALREADY_EXISTS("Pin already exists"),
//    PIN_INVALID_REQUEST("Invalid Pin request"),
//    COMMENT_NOT_FOUND("Comment not found"),
//    COMMENT_ALREADY_EXISTS("Comment already exists"),
//    COMMENT_INVALID_REQUEST("Invalid Comment request"),
//    USER_NOT_FOUND("User not found"),
//    USER_ALREADY_EXISTS("User already exists"),
//    USER_INVALID_REQUEST("Invalid User request"),
//    RESTAURANT_NOT_FOUND("Restaurant not found"),
//    RESTAURANT_ALREADY_EXISTS("Restaurant already exists"),
//    RESTAURANT_INVALID_REQUEST("Invalid Restaurant request");
    ALEADY_EXISTS("이미 존재하는 데이터입니다."),
    PIN_NOT_FOUND("해당 핀 정보를 찾을 수 없습니다."),
    COMMNET_NOT_FOUND("해당 댓글 정보를 찾을 수 없습니다."),

    USER_NOT_FOUND("해당 유저 정보를 찾을 수 없습니다.");

    private final String message;

    CustomExcpMsgs(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
