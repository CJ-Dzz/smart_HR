/**
 * 文档分块实体
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.rag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChunk {

    /**
     * 分块 ID
     */
    private String id;

    /**
     * 原始文档 ID
     */
    private String documentId;

    /**
     * 分块内容
     */
    private String content;

    /**
     * 分块索引（在原文档中的位置）
     */
    private int chunkIndex;

    /**
     * 元数据
     */
    private Map<String, Object> metadata;
}


