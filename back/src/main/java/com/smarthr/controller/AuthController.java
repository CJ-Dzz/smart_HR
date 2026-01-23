/**
 * 认证控制器
 * 处理登录、注册、Token 刷新等 API
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.controller;

import com.smarthr.dto.ApiResponse;
import com.smarthr.dto.auth.AuthResponse;
import com.smarthr.dto.auth.LoginRequest;
import com.smarthr.dto.auth.RegisterRequest;
import com.smarthr.security.UserPrincipal;
import com.smarthr.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录、注册、Token 刷新等接口")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，返回 JWT Token")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for user: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ApiResponse.success(response, "登录成功");
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户并返回 JWT Token")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request for user: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        return ApiResponse.success(response, "注册成功");
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新 Token", description = "使用 Refresh Token 获取新的 Access Token")
    public ApiResponse<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        log.info("Token refresh request");
        AuthResponse response = authService.refreshToken(refreshToken);
        return ApiResponse.success(response, "Token 刷新成功");
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户", description = "获取当前登录用户的信息")
    public ApiResponse<AuthResponse.UserInfo> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ApiResponse.unauthorized("未登录或 Token 已失效");
        }
        log.info("Get current user: {}", userPrincipal.getUsername());
        AuthResponse.UserInfo userInfo = authService.getCurrentUser(userPrincipal);
        return ApiResponse.success(userInfo);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出（客户端清除 Token 即可）")
    public ApiResponse<Void> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ApiResponse.unauthorized("未登录或 Token 已失效");
        }
        log.info("User logout: {}", userPrincipal.getUsername());
        // JWT 是无状态的，登出只需客户端清除 Token。可扩展为黑名单机制。
        return ApiResponse.successMessage("登出成功");
    }
}
