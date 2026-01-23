/**
 * RAG 检索增强生成服务
 * 整合向量存储和 LLM 实现智能检索和问答
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.rag;

import com.smarthr.service.ai.ModelRouter;
import com.smarthr.service.vector.MilvusVectorStore;
import com.smarthr.service.vector.VectorDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RAGService {

    private final EmbeddingService embeddingService;
    private final DocumentSplitter documentSplitter;
    private final ModelRouter modelRouter;
    
    // 可选依赖，使用 @Autowired(required = false) 或通过构造器处理
    private final Optional<MilvusVectorStore> milvusVectorStore;

    /**
     * 索引简历文档到 Milvus
     */
    public void indexResume(String resumeId, String content, Map<String, Object> metadata) {
        if (milvusVectorStore.isEmpty()) {
            log.warn("Milvus is not available, skipping resume indexing");
            return;
        }

        try {
            // 分块
            List<DocumentChunk> chunks = documentSplitter.split(resumeId, content, metadata);
            
            // 向量化并存储
            List<VectorDocument> documents = new ArrayList<>();
            for (DocumentChunk chunk : chunks) {
                float[] embedding = embeddingService.embed(chunk.getContent());
                
                VectorDocument doc = VectorDocument.builder()
                        .id(chunk.getId())
                        .content(chunk.getContent())
                        .embedding(embedding)
                        .metadata(chunk.getMetadata())
                        .build();
                
                documents.add(doc);
            }
            
            milvusVectorStore.get().addDocuments(documents);
            log.info("Indexed resume {} with {} chunks to Milvus", resumeId, chunks.size());
            
        } catch (Exception e) {
            log.error("Failed to index resume: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to index resume", e);
        }
    }



}
