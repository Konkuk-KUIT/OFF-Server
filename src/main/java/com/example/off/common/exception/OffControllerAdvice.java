package com.example.off.common.exception;

import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OffControllerAdvice {
    /**
     * 커스텀 예외 처리 (비즈니스 로직 중 발생하는 예외)
     */
    @ExceptionHandler(OffException.class)
    public ResponseEntity<BaseResponse<Void>> handleBaseException(OffException e) {
        log.error("BaseException: {}", e.getMessage());
        ResponseCode responseCode = e.getResponseCode();
        return ResponseEntity
                .status(HttpStatus.valueOf(responseCode.getCode())) // 혹은 상황에 맞는 HTTP Status
                .body(new BaseResponse<>(responseCode));
    }

    /**
     * 예상치 못한 일반적인 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        log.error("Internal Server Error: ", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>(ResponseCode.INTERNAL_SERVER_ERROR)); // Enum에 미리 정의 필요
    }
}
