package com.smarthr.service.interview;

import com.smarthr.service.rag.EmbeddingService;
import com.smarthr.service.vector.QuestionBankVectorStore;
import com.smarthr.service.vector.VectorDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionBankService {

    private final EmbeddingService embeddingService;
    private final Optional<QuestionBankVectorStore> questionBankVectorStore;

    /**
     * 在题库中检索与技能/业务域相关的题目
     */
    public List<QuestionBankEntry> searchSimilarQuestions(List<String> skills,
                                                          String difficulty,
                                                          String questionType,
                                                          String businessDomain,
                                                          int topK) {
        if (questionBankVectorStore.isEmpty()) {
            log.warn("Question bank Milvus collection unavailable, skip retrieval");
            return Collections.emptyList();
        }

        try {
            String queryText = buildQuery(skills, difficulty, questionType, businessDomain);
            float[] queryEmbedding = embeddingService.embed(queryText);
            List<VectorDocument> docs = questionBankVectorStore.get().similaritySearch(queryEmbedding, topK);
            return docs.stream()
                    .map(doc -> QuestionBankEntry.fromMetadata(
                            doc.getId(),
                            doc.getContent(),
                            Optional.ofNullable(doc.getMetadata()).orElse(Collections.emptyMap()),
                            doc.getScore()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Question bank search failed: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private String buildQuery(List<String> skills,
                              String difficulty,
                              String questionType,
                              String businessDomain) {
        String skillText = skills == null ? "" : String.join(", ", skills);
        return String.format("Smart-HR 科技 面试题 %s %s 难度=%s 题型=%s 业务域=%s",
                skillText,
                "企业金融/支付",
                difficulty,
                questionType,
                businessDomain != null ? businessDomain : "企业金融/支付");
    }

    /**
     * 将题库命中结果格式化为 Prompt 上下文
     */
    public String formatContext(List<QuestionBankEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return "【未命中内部题库，需回退生成】";
        }
        StringBuilder sb = new StringBuilder();
        for (QuestionBankEntry entry : entries) {
            sb.append("- 题目：").append(entry.getQuestion()).append("\n");
            if (entry.getAnswer() != null && !entry.getAnswer().isEmpty()) {
                sb.append("  要点：").append(entry.getAnswer()).append("\n");
            }
            sb.append("  标签：");
            sb.append("domain=").append(Optional.ofNullable(entry.getDomain()).orElse("企业金融/支付"));
            if (entry.getDifficulty() != null) {
                sb.append(", difficulty=").append(entry.getDifficulty());
            }
            if (entry.getType() != null) {
                sb.append(", type=").append(entry.getType());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
