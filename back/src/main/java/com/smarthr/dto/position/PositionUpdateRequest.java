/**
 * 更新岗位请求 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.dto.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionUpdateRequest {
    private String title;
    private String company;
    private String salaryRange;
    private String experience;
    private String education;
    private String location;
    private String responsibilities;
    private String requirements;
    private List<String> skills;
}

