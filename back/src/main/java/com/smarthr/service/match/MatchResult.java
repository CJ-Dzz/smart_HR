/**
 * 匹配结果实体
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.service.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {

    /**
     * 简历 ID
     */
    private String resumeId;

    /**
     * 岗位 ID
     */
    private String positionId;

    /**
     * 综合得分（0-100）
     */
    private float finalScore;

    /**
     * RAG 语义相似度得分（0-100）
     */
    private float ragScore;

    /**
     * 知识图谱技能匹配得分（0-100）
     */
    private float graphScore;

    /**
     * LLM 综合评估得分（0-100）
     */
    private float llmScore;

    /**
     * 匹配的技能列表
     */
    private List<String> matchedSkills;

    /**
     * 缺失的技能列表
     */
    private List<String> missingSkills;

    /**
     * 候选人额外技能
     */
    private List<String> extraSkills;

    /**
     * LLM 评估报告
     */
    private String llmReport;

    /**
     * 详细评分明细
     */
    private Map<String, Object> scoreDetails;

    /**
     * 推荐指数（1-5星）
     */
    private int recommendLevel;

    /**
     * 匹配等级（A/B/C/D）
     */
    private String matchGrade;
}


