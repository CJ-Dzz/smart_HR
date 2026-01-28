/**
 * 岗位创建请求
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.position;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionCreateRequest {

    @NotBlank(message = "岗位名称不能为空")
    private String title;

    private String company;
    private String salaryRange;
    private String experience;
    private String education;
    private String location;

    @NotBlank(message = "岗位职责不能为空")
    private String responsibilities;

    @NotBlank(message = "岗位要求不能为空")
    private String requirements;

    private List<String> skills;
}
