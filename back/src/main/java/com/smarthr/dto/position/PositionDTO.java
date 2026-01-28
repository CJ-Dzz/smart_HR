/**
 * 岗位 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.position;

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
public class PositionDTO {

    private Long id;
    private String title;
    private String company;
    private String salaryRange;
    private String experience;
    private String education;
    private String location;
    private String responsibilities;
    private String requirements;
    private List<String> skills;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
