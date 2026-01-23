/**
 * 向量文档实体
 * 表示一个可被向量化和检索的文档
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.vector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorDocument {

    /**
     * 文档唯一标识
     */
    private String id;

    /**
     * 文档内容
     */
    private String content;

    /**
     * 向量嵌入
     */
    private float[] embedding;

    /**
     * 元数据
     */
    private Map<String, Object> metadata;

    /**
     * 相似度分数（检索结果时填充）
     */
    private Float score;
}


