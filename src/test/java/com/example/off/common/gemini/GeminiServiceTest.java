package com.example.off.common.gemini;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeminiServiceTest {

    @Autowired
    private GeminiService geminiService;

    @Disabled("외부 Gemini API 의존 — GEMINI_API_KEY 환경변수 설정 후 수동 실행")
    @Test
    void 프로젝트_서비스_요약_생성_테스트() {
        String prompt = """
                당신은 IT 프로젝트 전문 PM입니다.
                사용자가 입력한 `서비스 설명`과 `요구사항`을 분석하여 다음 두 가지 섹션으로 구성된 상세 기획안을 작성해주세요.

                **Section 1. 서비스 상세 정의**
                - 이 서비스가 무엇인지, 주요 타겟과 핵심 가치가 무엇인지 명확하게 요약 설명.

                **Section 2. 단계별 프로젝트 실행 계획**
                - 프로젝트를 완성하기 위한 과정을 **[기획 - 디자인 - 개발 - 테스트/배포]** 등의 단계(Phase)로 나누세요.
                - 각 단계별로 수행해야 할 **핵심 Task**와 그에 따른 **세부 할일(Sub-task)**을 구체적으로 나열하세요.
                - 개발 단계에서는 기술적인 내용(DB설계, API구현 등)을 포함하세요.

                **출력 형식:**
                - 마크다운(Markdown) 형식을 사용하여 가독성 좋게 출력할 것.
                - 불필요한 서론/결론 없이 본론 내용만 출력할 것.

                ---
                [서비스 설명]
                대학생들이 팀 프로젝트 파트너를 찾고, 프로젝트를 함께 관리할 수 있는 협업 매칭 플랫폼입니다.
                사용자는 자신의 기술 스택과 포트폴리오를 등록하고, 원하는 역할(개발자, 디자이너, 기획자)의
                파트너를 검색하여 프로젝트에 초대할 수 있습니다.

                [요구사항]
                1. 회원가입/로그인 (소셜 로그인 포함)
                2. 프로필 등록 (기술 스택, 포트폴리오, 경력)
                3. 프로젝트 생성 및 파트너 모집 공고 등록
                4. 역할/기술 기반 파트너 매칭 및 추천
                5. 실시간 채팅 (1:1, 그룹)
                6. 프로젝트 태스크 관리 (칸반보드)
                7. 프로젝트 완료 후 상호 평가 시스템
                """;

        System.out.println("=== Gemini API 호출 시작 ===");
        String result = geminiService.generateText(prompt);
        System.out.println("=== Gemini API 응답 ===");
        System.out.println(result);
        System.out.println("=== 끝 ===");
    }
}
