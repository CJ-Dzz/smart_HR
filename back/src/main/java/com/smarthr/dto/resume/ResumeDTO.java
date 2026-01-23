/**
 * 简历 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.resume;

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
public class ResumeDTO {

    private Long id;
    private Long userId;
    private String fileName;
    private String filePath;
    private String content;
    private List<String> extractedSkills;
    private LocalDateTime createdAt;
}
