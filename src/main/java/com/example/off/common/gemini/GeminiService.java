package com.example.off.common.gemini;

import com.example.off.common.exception.OffException;
import com.example.off.common.gemini.dto.GeminiRequest;
import com.example.off.common.gemini.dto.GeminiResponse;
import com.example.off.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {
    private final RestClient geminiRestClient;
    private final GeminiProperties geminiProperties;

    public String generateText(String prompt) {
        if (geminiProperties.apiKey() == null || geminiProperties.apiKey().isBlank()) {
            log.warn("Gemini API 키가 설정되지 않았습니다. 빈 문자열을 반환합니다.");
            return "";
        }

        String requestUrl = geminiProperties.url() + "?key=" + geminiProperties.apiKey();
        GeminiRequest request = GeminiRequest.of(prompt);

        try {
            GeminiResponse response = geminiRestClient.post()
                    .uri(requestUrl)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response == null) {
                throw new OffException(ResponseCode.GEMINI_API_ERROR);
            }

            return response.getText();
        } catch (RestClientException e) {
            log.error("Gemini API 호출 실패: {}", e.getMessage(), e);
            throw new OffException(ResponseCode.GEMINI_API_ERROR);
        }
    }
}