/**
 * Milvus 配置属性类
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "smart-hr.milvus")
public class MilvusProperties {

    /**
     * 是否启用 Milvus
     */
    private boolean enabled = true;

    /**
     * Milvus 主机地址
     */
    private String host = "localhost";

    /**
     * Milvus 端口
     */
    private int port = 19530;

    /**
     * 数据库名称
     */
    private String databaseName = "default";

    /**
     * 集合名称（简历向量）
     */
    private String collectionName = "smart_hr_resumes";

    /**
     * 面试题库集合名称（与简历集合隔离）
     */
    private String questionCollectionName = "smart_hr_questions";

    /**
     * 向量维度
     */
    private int embeddingDimension = 1536;

    /**
     * 索引类型
     */
    private String indexType = "IVF_FLAT";

    /**
     * 度量类型
     */
    private String metricType = "COSINE";

    /**
     * nlist 参数（索引构建参数）
     */
    private int nlist = 1024;

    /**
     * nprobe 参数（搜索参数）
     */
    private int nprobe = 16;
}
