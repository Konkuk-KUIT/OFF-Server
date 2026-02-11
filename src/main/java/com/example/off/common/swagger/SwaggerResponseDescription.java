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

    GET_MY_PROFILE(new LinkedHashSet<>(Set.of(
            INVALID_TOKEN,
            MEMBER_NOT_FOUND,
            INTERNAL_SERVER_ERROR
    ))),

    GET_MY_PROJECTS(new LinkedHashSet<>(Set.of(
            INVALID_TOKEN,
            MEMBER_NOT_FOUND
    ))),

    UPDATE_PROFILE(new LinkedHashSet<>(Set.of(
            MEMBER_NOT_FOUND,
            DUPLICATE_NICKNAME,
            DUPLICATE_EMAIL,
            INVALID_INPUT_VALUE
    ))),


    GET_HOME(new LinkedHashSet<>(Set.of(
            MEMBER_NOT_FOUND
    ))),

    GET_PROJECT_DETAIL(new LinkedHashSet<>(Set.of(
            PROJECT_NOT_FOUND
    ))),

    UPDATE_INTRODUCTION(new LinkedHashSet<>(Set.of(
            PROJECT_NOT_FOUND,
            UNAUTHORIZED_ACCESS
    ))),

    COMPLETE_PROJECT(new LinkedHashSet<>(Set.of(
            PROJECT_NOT_FOUND,
            UNAUTHORIZED_ACCESS,
            PROJECT_ALREADY_COMPLETED
    ))),

    CREATE_TASK(new LinkedHashSet<>(Set.of(
            PROJECT_NOT_FOUND,
            UNAUTHORIZED_ACCESS
    ))),

    UPDATE_TASK(new LinkedHashSet<>(Set.of(
            TASK_NOT_FOUND,
            UNAUTHORIZED_ACCESS
    ))),

    DELETE_TASK(new LinkedHashSet<>(Set.of(
            TASK_NOT_FOUND,
            UNAUTHORIZED_ACCESS
    ))),

    TOGGLE_TODO(new LinkedHashSet<>(Set.of(
            TASK_NOT_FOUND,
            TODO_NOT_FOUND
    ))),

    INVITE_PARTNER(new LinkedHashSet<>(Set.of(
            PROJECT_NOT_FOUND,
            MEMBER_NOT_FOUND,
            RECRUIT_NOT_FOUND,
            RECRUIT_CLOSED,
            ALREADY_APPLIED,
            UNAUTHORIZED_ACCESS
    ))),

    ACCEPT_INVITATION(new LinkedHashSet<>(Set.of(
            APPLICATION_NOT_FOUND,
            INVALID_APPLICATION_STATUS,
            RECRUIT_CLOSED,
            ALREADY_PROJECT_MEMBER
    ))),

    APPLY_PROJECT(new LinkedHashSet<>(Set.of(
            PROJECT_NOT_FOUND,
            RECRUIT_NOT_FOUND,
            RECRUIT_CLOSED,
            ALREADY_APPLIED
    ))),

    ACCEPT_APPLICATION(new LinkedHashSet<>(Set.of(
            PROJECT_NOT_FOUND,
            APPLICATION_NOT_FOUND,
            INVALID_APPLICATION_STATUS,
            UNAUTHORIZED_ACCESS
    ))),

    GET_PARTNER_PROFILE(new LinkedHashSet<>(Set.of(
            MEMBER_NOT_FOUND
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
