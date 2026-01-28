/**
 * 岗位管理控制器
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.controller.hr;

import com.smarthr.dto.ApiResponse;
import com.smarthr.dto.position.PositionCreateRequest;
import com.smarthr.dto.position.PositionDTO;
import com.smarthr.security.UserPrincipal;
import com.smarthr.service.hr.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    /**
     * 创建岗位
     */
    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApiResponse<PositionDTO>> createPosition(
            @Valid @RequestBody PositionCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        PositionDTO position = positionService.createPosition(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success(position, "岗位创建成功"));
    }

    /**
     * 更新岗位
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApiResponse<PositionDTO>> updatePosition(
            @PathVariable Long id,
            @Valid @RequestBody PositionCreateRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        PositionDTO position = positionService.updatePosition(id, request, user.getId());
        return ResponseEntity.ok(ApiResponse.success(position, "岗位更新成功"));
    }

    /**
     * 删除岗位
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ApiResponse<Void>> deletePosition(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        positionService.deletePosition(id, user.getId());
        return ResponseEntity.ok(ApiResponse.successMessage("岗位删除成功"));
    }

    /**
     * 获取岗位详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR','INTERVIEWER')")
    public ResponseEntity<ApiResponse<PositionDTO>> getPosition(@PathVariable Long id) {
        PositionDTO position = positionService.getPosition(id);
        return ResponseEntity.ok(ApiResponse.success(position));
    }

    /**
     * 分页查询岗位列表
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('HR','INTERVIEWER')")
    public ResponseEntity<ApiResponse<Page<PositionDTO>>> listPositions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        Page<PositionDTO> positions = positionService.listPositions(page, size, keyword);
        return ResponseEntity.ok(ApiResponse.success(positions));
    }

    /**
     * 获取所有岗位（用于下拉选择）
     */
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('HR','INTERVIEWER')")
    public ResponseEntity<ApiResponse<List<PositionDTO>>> getAllPositions() {
        List<PositionDTO> positions = positionService.getAllPositions();
        return ResponseEntity.ok(ApiResponse.success(positions));
    }
}
