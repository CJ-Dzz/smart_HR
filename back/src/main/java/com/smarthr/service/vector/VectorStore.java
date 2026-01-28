/**
 * 向量存储接口
 * 定义向量存储的基本操作
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.vector;

import java.util.List;

public interface VectorStore {

    /**
     * 获取存储标识
     */
    String getStoreId();

    /**
     * 添加文档
     */
    void addDocument(VectorDocument document);

    /**
     * 批量添加文档
     */
    void addDocuments(List<VectorDocument> documents);

    /**
     * 根据 ID 删除文档
     */
    void deleteDocument(String id);

    /**
     * 批量删除文档
     */
    void deleteDocuments(List<String> ids);

    /**
     * 相似度搜索
     *
     * @param queryEmbedding 查询向量
     * @param topK          返回结果数量
     * @return 相似文档列表
     */
    List<VectorDocument> similaritySearch(float[] queryEmbedding, int topK);

    /**
     * 相似度搜索（带过滤条件）
     *
     * @param queryEmbedding 查询向量
     * @param topK          返回结果数量
     * @param filter        过滤条件
     * @return 相似文档列表
     */
    List<VectorDocument> similaritySearch(float[] queryEmbedding, int topK, String filter);

    /**
     * 根据 ID 获取文档
     */
    VectorDocument getDocument(String id);

    /**
     * 检查文档是否存在
     */
    boolean exists(String id);

    /**
     * 获取文档数量
     */
    long count();
}


