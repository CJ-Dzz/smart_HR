/**
 * 向量化服务
 * 将文本转换为向量表示
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;

    /**
     * 将单个文本转换为向量
     */
    public float[] embed(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        try {
            EmbeddingResponse response = embeddingModel.embedForResponse(List.of(text));
            float[] embedding = response.getResult().getOutput();
            log.debug("Embedded text of length {} to vector of dimension {}", text.length(), embedding.length);
            return embedding;
        } catch (Exception e) {
            log.error("Failed to embed text: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to embed text", e);
        }
    }

    /**
     * 批量将文本转换为向量
     */
    public List<float[]> embedBatch(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            EmbeddingResponse response = embeddingModel.embedForResponse(texts);
            List<float[]> embeddings = new ArrayList<>();
            response.getResults().forEach(result -> embeddings.add(result.getOutput()));
            log.debug("Embedded {} texts to vectors", texts.size());
            return embeddings;
        } catch (Exception e) {
            log.error("Failed to embed texts batch: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to embed texts batch", e);
        }
    }

    /**
     * 获取向量维度
     */
    public int getDimensions() {
        // 阿里云 text-embedding-v2 默认 1536 维
        return 1536;
    }
}


