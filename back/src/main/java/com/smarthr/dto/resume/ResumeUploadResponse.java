/**
 * 简历上传响应
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeUploadResponse {

    private Long resumeId;
    private String fileName;
    private String parsedContent;
    private List<String> extractedSkills;
    private String message;
}


