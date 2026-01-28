/**
 * 简历管理控制器
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.controller.hr;

import com.smarthr.dto.ApiResponse;
import com.smarthr.dto.resume.ResumeDTO;
import com.smarthr.dto.resume.ResumeUploadResponse;
import com.smarthr.security.UserPrincipal;
import com.smarthr.service.hr.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/hr/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('HR')")
public class ResumeController {

    private final ResumeService resumeService;

    /**
     * 上传简历
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ResumeUploadResponse>> uploadResume(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserPrincipal user) {
        ResumeUploadResponse response = resumeService.uploadResume(file, user.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "简历上传成功"));
    }

    /**
     * 获取简历详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeDTO>> getResume(@PathVariable Long id) {
        ResumeDTO resume = resumeService.getResume(id);
        return ResponseEntity.ok(ApiResponse.success(resume));
    }

    /**
     * 分页查询简历列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ResumeDTO>>> listResumes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        Page<ResumeDTO> resumes = resumeService.listResumes(page, size, keyword);
        return ResponseEntity.ok(ApiResponse.success(resumes));
    }

    /**
     * 删除简历
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal user) {
        resumeService.deleteResume(id, user.getId());
        return ResponseEntity.ok(ApiResponse.successMessage("简历删除成功"));
    }

    /**
     * 获取所有简历（用于下拉选择）
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ResumeDTO>>> getAllResumes() {
        List<ResumeDTO> resumes = resumeService.getAllResumes();
        return ResponseEntity.ok(ApiResponse.success(resumes));
    }
}

