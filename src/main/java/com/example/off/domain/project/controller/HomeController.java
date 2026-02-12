package com.example.off.domain.project.controller;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.exception.OffException;
import com.example.off.common.response.BaseResponse;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.SwaggerResponseDescription;
import com.example.off.domain.project.dto.HomeResponse;
import com.example.off.domain.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Home", description = "홈 화면 API")
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final ProjectService projectService;

    @Operation(summary = "홈 화면 조회", description = "진행 중인 프로젝트 목록 및 파트너 추천을 조회합니다.")
    @GetMapping("/home")
    @CustomExceptionDescription(SwaggerResponseDescription.GET_HOME)
    public BaseResponse<HomeResponse> getHome(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long memberId = getMemberId(httpServletRequest);
        return BaseResponse.ok(projectService.getHome(memberId, PageRequest.of(page, size)));
    }

    private Long getMemberId(HttpServletRequest req) {
        Object attr = req.getAttribute("memberId");
        if (attr instanceof Long id) return id;
        throw new OffException(ResponseCode.INVALID_TOKEN);
    }
}
