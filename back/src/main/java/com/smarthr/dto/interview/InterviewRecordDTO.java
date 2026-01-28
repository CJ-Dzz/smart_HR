/**
 * 面试记录 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRecordDTO {

    private Long id;
    private Long positionId;
    private String positionTitle;
    private Long userId;
    private String difficulty;
    private String questionType;
    private List<InterviewQuestionDTO> questions;
    private LocalDateTime createdAt;
}


