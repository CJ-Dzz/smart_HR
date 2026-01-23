package com.smarthr.service.interview;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class QuestionBankEntry {
    private String id;
    private String question;
    private String answer;
    private String difficulty;
    private String type;
    private String domain;
    private String subdomain;
    private String role;
    private List<String> tags;
    private LocalDateTime updatedAt;
    private Float score;

    public static QuestionBankEntry fromMetadata(String id, String content, Map<String, Object> metadata, Float score) {
        return QuestionBankEntry.builder()
                .id(id)
                .question(get(metadata, "question", content))
                .answer(get(metadata, "answer", ""))
                .difficulty(get(metadata, "difficulty", null))
                .type(get(metadata, "type", null))
                .domain(get(metadata, "domain", null))
                .subdomain(get(metadata, "subdomain", null))
                .role(get(metadata, "role", null))
                .tags((List<String>) metadata.getOrDefault("tags", null))
                .updatedAt(parseDate(metadata.get("updatedAt")))
                .score(score)
                .build();
    }

    private static String get(Map<String, Object> metadata, String key, String defaultValue) {
        Object val = metadata.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    private static LocalDateTime parseDate(Object val) {
        if (val == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(val.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
