package com.example.off.common.swagger;

import com.example.off.common.response.ResponseCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.off.common.response.ResponseCode.*;

@Getter
public enum SwaggerResponseDescription {

    TEST(new LinkedHashSet<>(Set.of(
            TEST_EXCEPTION
    ))),

    GET_NOTIFICATIONS(new LinkedHashSet<>(Set.of(
            NOTIFICATION_NOT_FOUND
    ))),

    READ_NOTIFICATIONS(new LinkedHashSet<>(Set.of(
            BAD_NOTIFICATION_REQUEST
    ))),

    GET_CHAT_ROOMS(new HashSet<>(Set.of(
            OPPONENT_NOT_FOUND
    ))),

    GET_CHAT_MESSAGES(new HashSet<>(Set.of(
            OPPONENT_NOT_FOUND
    ))),

    SEND_MESSAGES(new HashSet<>(Set.of(
            OPPONENT_NOT_FOUND,
            MEMBER_NOT_FOUND
    ))),

    CREATE_ROOM_AND_SEND_MESSAGES(new HashSet<>(Set.of(
            MEMBER_NOT_FOUND,
            OPPONENT_NOT_FOUND
    ))),

    ESTIMATE_PROJECT(new HashSet<>(Set.of(
            MEMBER_NOT_FOUND,
            INVALID_PROJECT_TYPE,
            INVALID_ROLE
    ))),

    CONFIRM_PROJECT(new HashSet<>(Set.of(
            MEMBER_NOT_FOUND,
            INVALID_PROJECT_TYPE,
            INVALID_ROLE
    ))),

    SIGNUP(new LinkedHashSet<>(Set.of(
            DUPLICATE_EMAIL
    ))),

    LOGIN(new LinkedHashSet<>(Set.of(
        INVALID_LOGIN_CREDENTIALS
    ))),

    DEFAULT(new LinkedHashSet<>());

    private final Set<ResponseCode> responseCodeSet;

    SwaggerResponseDescription(Set<ResponseCode> responseCodeSet) {
        responseCodeSet.addAll(new LinkedHashSet<>(Set.of(
                INVALID_PATH_VARIABLE_TYPE,
                BAD_REQUEST,
                API_NOT_FOUND,
                METHOD_NOT_ALLOWED,
                INTERNAL_SERVER_ERROR
        )));

        this.responseCodeSet = responseCodeSet;
    }
}
