package com.example.off.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public enum ResponseCode {
    // 테스트
    TEST_EXCEPTION(true, 100, "테스트용 예외입니다.", HttpStatus.OK),

    // global 요청 성공
    SUCCESS(true, 200, "요청에 성공하였습니다.", HttpStatus.OK),

    // 공통 에러
    INVALID_PATH_VARIABLE_TYPE(false, 400, "요청 경로에 포함된 값의 타입이 올바르지 않습니다. 올바른 형식으로 요청해주세요.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST(false, 400, "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    API_NOT_FOUND(false, 404, "존재하지 않는 API입니다.", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED(false, 405, "유효하지 않은 Http 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR(false, 500, "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 알림
    NOTIFICATION_NOT_FOUND(false, 404, "알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BAD_NOTIFICATION_REQUEST(false, 400, "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),

    // 채팅
    OPPONENT_NOT_FOUND(false, 404, "채팅 상대방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CHATROOM_NOT_FOUND(false, 404, "채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 회원
    MEMBER_NOT_FOUND(false, 404, "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 토큰
    INVALID_TOKEN(false, 400, "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(false, 404, "토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private boolean isSuccess;
    private int code;
    private String message;
    private HttpStatus httpStatus;
}