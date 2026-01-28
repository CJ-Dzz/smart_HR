/**
 * 生成面试题请求
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.interview;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateQuestionsRequest {

    /**
     * 岗位 ID（可选，根据岗位生成）
     */
    private Long positionId;

    /**
     * 技能列表（可选，根据技能生成）
     */
    private List<String> skills;

    /**
     * 难度等级：JUNIOR（初级）、MIDDLE（中级）、SENIOR（高级）
     */
    private String difficulty = "MIDDLE";

    /**
     * 生成题目数量
     */
    @Min(value = 1, message = "题目数量至少为1")
    @Max(value = 20, message = "题目数量最多为20")
    private Integer count = 5;

    /**
     * 题目类型：TECHNICAL（技术题）、BEHAVIORAL（行为题）、SCENARIO（情景题）、MIXED（混合）
     */
    private String questionType = "MIXED";

    /**
     * 是否包含参考答案
     */
    private Boolean includeAnswers = true;

    /**
     * 业务主域（默认企业金融/支付）
     */
    private String businessDomain = "企业金融/支付";
}
