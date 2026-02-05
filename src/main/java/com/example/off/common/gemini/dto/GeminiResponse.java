package com.example.off.common.gemini.dto;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) {

    public record Candidate(Content content) {
    }

    public record Content(List<Part> parts, String role) {
    }

    public record Part(String text) {
    }

    public String getText() {
        if (candidates == null || candidates.isEmpty()) {
            return "";
        }
        Candidate candidate = candidates.get(0);
        if (candidate == null || candidate.content() == null) {
            return "";
        }
        List<Part> parts = candidate.content().parts();
        if (parts == null || parts.isEmpty()) {
            return "";
        }
        Part firstPart = parts.get(0);
        return (firstPart == null || firstPart.text() == null) ? "" : firstPart.text();
    }
}
