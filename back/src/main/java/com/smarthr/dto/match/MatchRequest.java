/**
 * 匹配请求 DTO
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.dto.match;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {
    
    @NotNull(message = "简历 ID 不能为空")
    private Long resumeId;
    
    @NotNull(message = "岗位 ID 不能为空")
    private Long positionId;
}

