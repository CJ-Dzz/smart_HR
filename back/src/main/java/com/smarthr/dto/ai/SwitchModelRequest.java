/**
 * 切换 AI 模型请求
 *
 * @author QinFeng Luo
 * @date 2026/01/15
 */
package com.smarthr.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SwitchModelRequest {

    /**
     * 模型 ID
     */
    @NotBlank(message = "模型ID不能为空")
    private String modelId;
}
