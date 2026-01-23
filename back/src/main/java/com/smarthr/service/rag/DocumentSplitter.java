/**
 * 文档分块器
 * 将长文档分割成适合向量化的小块
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
public class DocumentSplitter {

    /**
     * 默认分块大小（字符数）
     */
    private static final int DEFAULT_CHUNK_SIZE = 500;

    /**
     * 默认重叠大小（字符数）
     */
    private static final int DEFAULT_OVERLAP_SIZE = 50;

    /**
     * 段落分隔符
     */
    private static final Pattern PARAGRAPH_PATTERN = Pattern.compile("\\n\\n+");

    /**
     * 句子分隔符
     */
    private static final Pattern SENTENCE_PATTERN = Pattern.compile("[。！？.!?]+");

    /**
     * 分割文档为多个块
     */
    public List<DocumentChunk> split(String documentId, String content, Map<String, Object> metadata) {
        return split(documentId, content, metadata, DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP_SIZE);
    }

    /**
     * 分割文档为多个块（自定义参数）
     */
    public List<DocumentChunk> split(String documentId, String content, Map<String, Object> metadata,
                                     int chunkSize, int overlapSize) {
        if (content == null || content.isEmpty()) {
            return Collections.emptyList();
        }

        List<DocumentChunk> chunks = new ArrayList<>();
        
        // 先按段落分割
        String[] paragraphs = PARAGRAPH_PATTERN.split(content);
        StringBuilder currentChunk = new StringBuilder();
        int chunkIndex = 0;

        for (String paragraph : paragraphs) {
            paragraph = paragraph.trim();
            if (paragraph.isEmpty()) {
                continue;
            }

            // 如果当前段落加上已有内容超过限制，先保存当前块
            if (currentChunk.length() + paragraph.length() > chunkSize && currentChunk.length() > 0) {
                chunks.add(createChunk(documentId, currentChunk.toString(), chunkIndex++, metadata));
                
                // 保留重叠部分
                String overlap = getOverlap(currentChunk.toString(), overlapSize);
                currentChunk = new StringBuilder(overlap);
            }

            // 如果单个段落就超过限制，需要进一步分割
            if (paragraph.length() > chunkSize) {
                // 先保存当前块
                if (currentChunk.length() > 0) {
                    chunks.add(createChunk(documentId, currentChunk.toString(), chunkIndex++, metadata));
                    currentChunk = new StringBuilder();
                }
                
                // 按句子分割大段落
                List<DocumentChunk> subChunks = splitLargeParagraph(documentId, paragraph, metadata, 
                        chunkIndex, chunkSize, overlapSize);
                chunks.addAll(subChunks);
                chunkIndex += subChunks.size();
            } else {
                if (currentChunk.length() > 0) {
                    currentChunk.append("\n\n");
                }
                currentChunk.append(paragraph);
            }
        }

        // 添加最后一个块
        if (currentChunk.length() > 0) {
            chunks.add(createChunk(documentId, currentChunk.toString(), chunkIndex, metadata));
        }

        log.debug("Document {} split into {} chunks", documentId, chunks.size());
        return chunks;
    }

    /**
     * 分割大段落
     */
    private List<DocumentChunk> splitLargeParagraph(String documentId, String paragraph, 
                                                     Map<String, Object> metadata, int startIndex,
                                                     int chunkSize, int overlapSize) {
        List<DocumentChunk> chunks = new ArrayList<>();
        
        // 按句子分割
        String[] sentences = SENTENCE_PATTERN.split(paragraph);
        StringBuilder currentChunk = new StringBuilder();
        int chunkIndex = startIndex;

        for (String sentence : sentences) {
            sentence = sentence.trim();
            if (sentence.isEmpty()) {
                continue;
            }

            if (currentChunk.length() + sentence.length() > chunkSize && currentChunk.length() > 0) {
                chunks.add(createChunk(documentId, currentChunk.toString(), chunkIndex++, metadata));
                String overlap = getOverlap(currentChunk.toString(), overlapSize);
                currentChunk = new StringBuilder(overlap);
            }

            if (currentChunk.length() > 0) {
                currentChunk.append("。");
            }
            currentChunk.append(sentence);
        }

        if (currentChunk.length() > 0) {
            chunks.add(createChunk(documentId, currentChunk.toString(), chunkIndex, metadata));
        }

        return chunks;
    }

    /**
     * 获取重叠部分
     */
    private String getOverlap(String text, int overlapSize) {
        if (text.length() <= overlapSize) {
            return text;
        }
        return text.substring(text.length() - overlapSize);
    }

    /**
     * 创建分块
     */
    private DocumentChunk createChunk(String documentId, String content, int index, Map<String, Object> metadata) {
        Map<String, Object> chunkMetadata = new HashMap<>();
        if (metadata != null) {
            chunkMetadata.putAll(metadata);
        }
        chunkMetadata.put("documentId", documentId);
        chunkMetadata.put("chunkIndex", index);

        return DocumentChunk.builder()
                .id(documentId + "_chunk_" + index)
                .documentId(documentId)
                .content(content)
                .chunkIndex(index)
                .metadata(chunkMetadata)
                .build();
    }
}


