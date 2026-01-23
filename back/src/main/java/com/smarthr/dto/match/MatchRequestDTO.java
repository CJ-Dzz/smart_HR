/**
 * 匹配请求 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.dto.match;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequestDTO {

    @NotNull(message = "简历ID不能为空")
    private Long resumeId;

    @NotNull(message = "岗位ID不能为空")
    private Long positionId;
}


