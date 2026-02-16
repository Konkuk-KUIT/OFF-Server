package com.example.off.common.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TossPaymentsClient {

    private final WebClient webClient;
    private final String authorization;

    public TossPaymentsClient(
            WebClient.Builder builder,
            @Value("${payment.secret.key}") String secretKey
    ) {
        this.webClient = builder
                .baseUrl("https://api.tosspayments.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String raw = secretKey + ":";
        this.authorization = "Basic " + Base64.getEncoder()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public TossConfirmResponse confirm(
            String paymentKey,
            String orderId,
            Long amount
    ) {
        TossConfirmRequest body =
                new TossConfirmRequest(paymentKey, orderId, amount);

        return webClient.post()
                .uri("/v1/payments/confirm")
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(TossConfirmResponse.class)
                .block();
    }

    private record TossConfirmRequest(
            String paymentKey,
            String orderId,
            Long amount
    ) {}
}