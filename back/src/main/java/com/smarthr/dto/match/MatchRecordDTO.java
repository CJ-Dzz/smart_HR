/**
 * 匹配记录 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.dto.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchRecordDTO {
    private Long id;
    private Long resumeId;
    private Long positionId;
    private String resumeFileName;
    private String positionTitle;
    private BigDecimal finalScore;
    private BigDecimal ragScore;
    private BigDecimal graphScore;
    private BigDecimal llmScore;
    private Map<String, Object> report;
    private LocalDateTime createdAt;
}

