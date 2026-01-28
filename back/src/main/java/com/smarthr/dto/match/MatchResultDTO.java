/**
 * 匹配结果 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultDTO {

    private Long id;
    private Long resumeId;
    private Long positionId;
    private String resumeName;
    private String positionTitle;
    
    private Float finalScore;
    private Float ragScore;
    private Float graphScore;
    private Float llmScore;
    
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private List<String> extraSkills;
    
    private String llmReport;
    private String matchGrade;
    private Integer recommendLevel;
    
    private Map<String, Object> scoreDetails;
    private LocalDateTime createdAt;
}


