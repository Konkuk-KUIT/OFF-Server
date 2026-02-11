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
    INVALID_INPUT_VALUE(false, 400, "입력값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

    // 알림
    NOTIFICATION_NOT_FOUND(false, 404, "알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BAD_NOTIFICATION_REQUEST(false, 400, "유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),

    // 채팅
    OPPONENT_NOT_FOUND(false, 404, "채팅 상대방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CHATROOM_NOT_FOUND(false, 404, "채팅방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 회원
    MEMBER_NOT_FOUND(false, 404, "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL(false, 409, "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICKNAME(false, 409, "이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
    INVALID_LOGIN_CREDENTIALS(false, 401, "이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),


    // Gemini
    GEMINI_API_ERROR(false, 502, "Gemini API 호출에 실패하였습니다.", HttpStatus.BAD_GATEWAY),

    // 프로젝트
    PROJECT_NOT_FOUND(false, 404, "프로젝트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PROJECT_TYPE(false, 400, "유효하지 않은 프로젝트 유형입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ROLE(false, 400, "유효하지 않은 직무입니다.", HttpStatus.BAD_REQUEST),
    RECRUIT_NOT_FOUND(false, 404, "파트너 모집 공고를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    APPLICATION_NOT_FOUND(false, 404, "파트너 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 토큰
    INVALID_TOKEN(false, 400, "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
    TOKEN_NOT_FOUND(false, 404, "토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 결제
    PAYLOG_NOT_FOUND(false, 404, "결제 사항을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PAY_STATUS(false, 400, "결제 사항이 유효하지 않습니다", HttpStatus.BAD_REQUEST),
    TOSS_CONFIRM_FAILED(false, 502, "결제 승인에 실패하였습니다.", HttpStatus.BAD_GATEWAY),

    // 파트너 매칭
    RECRUIT_CLOSED(false, 400, "모집이 마감된 공고입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_APPLIED(false, 409, "이미 지원한 공고입니다.", HttpStatus.CONFLICT),
    INVALID_APPLICATION_STATUS(false, 400, "유효하지 않은 지원 상태입니다.", HttpStatus.BAD_REQUEST),
    ALREADY_PROJECT_MEMBER(false, 409, "이미 프로젝트 멤버입니다.", HttpStatus.CONFLICT),

    // 태스크
    TASK_NOT_FOUND(false, 404, "태스크를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    TODO_NOT_FOUND(false, 404, "할일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 권한
    UNAUTHORIZED_ACCESS(false, 403, "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    PROJECT_ALREADY_COMPLETED(false, 400, "이미 완료된 프로젝트입니다.", HttpStatus.BAD_REQUEST);

    private boolean isSuccess;
    private int code;
    private String message;
    private HttpStatus httpStatus;
}