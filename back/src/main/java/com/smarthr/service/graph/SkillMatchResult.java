/**
 * 技能匹配结果
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillMatchResult {

    /**
     * 匹配度分数（0-1）
     */
    private float score;

    /**
     * 匹配的技能
     */
    private List<String> matchedSkills;

    /**
     * 缺失的技能
     */
    private List<String> missingSkills;

    /**
     * 候选人的额外技能
     */
    private List<String> extraSkills;
}


