/**
 * 匹配分析控制器
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.controller.hr;

import com.smarthr.dto.ApiResponse;
import com.smarthr.dto.match.MatchRequestDTO;
import com.smarthr.dto.match.MatchResultDTO;
import com.smarthr.security.UserPrincipal;
import com.smarthr.service.hr.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/match")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HR')")
public class MatchController {

    private final MatchService matchService;

    /**
     * 执行单次匹配
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MatchResultDTO>> match(
            @Valid @RequestBody MatchRequestDTO request,
            @AuthenticationPrincipal UserPrincipal user) {
        MatchResultDTO result = matchService.match(request, user.getId());
        return ResponseEntity.ok(ApiResponse.success(result, "匹配完成"));
    }

    /**
     * 为简历匹配所有岗位
     */
    @PostMapping("/resume/{resumeId}/positions")
    public ResponseEntity<ApiResponse<List<MatchResultDTO>>> matchResumeToPositions(
            @PathVariable Long resumeId,
            @AuthenticationPrincipal UserPrincipal user) {
        List<MatchResultDTO> results = matchService.matchResumeToPositions(resumeId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(results, "匹配完成"));
    }

    /**
     * 为岗位匹配所有简历
     */
    @PostMapping("/position/{positionId}/resumes")
    public ResponseEntity<ApiResponse<List<MatchResultDTO>>> matchPositionToResumes(
            @PathVariable Long positionId,
            @AuthenticationPrincipal UserPrincipal user) {
        List<MatchResultDTO> results = matchService.matchPositionToResumes(positionId, user.getId());
        return ResponseEntity.ok(ApiResponse.success(results, "匹配完成"));
    }

    /**
     * 获取匹配记录详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MatchResultDTO>> getMatchRecord(@PathVariable Long id) {
        MatchResultDTO result = matchService.getMatchRecord(id);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 分页查询匹配记录
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MatchResultDTO>>> listMatchRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MatchResultDTO> records = matchService.listMatchRecords(page, size);
        return ResponseEntity.ok(ApiResponse.success(records));
    }

    /**
     * 获取简历的匹配历史
     */
    @GetMapping("/resume/{resumeId}/history")
    public ResponseEntity<ApiResponse<List<MatchResultDTO>>> getResumeMatchHistory(
            @PathVariable Long resumeId) {
        List<MatchResultDTO> results = matchService.getResumeMatchHistory(resumeId);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    /**
     * 获取岗位的匹配历史
     */
    @GetMapping("/position/{positionId}/history")
    public ResponseEntity<ApiResponse<List<MatchResultDTO>>> getPositionMatchHistory(
            @PathVariable Long positionId) {
        List<MatchResultDTO> results = matchService.getPositionMatchHistory(positionId);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}

