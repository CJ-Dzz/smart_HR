/**
 * JWT 配置属性类
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "smart-hr.jwt")
public class JwtProperties {

    /**
     * JWT 密钥
     */
    private String secret;

    /**
     * Token 过期时间（毫秒），默认 24 小时
     */
    private Long expiration = 86400000L;

    /**
     * Refresh Token 过期时间（毫秒），默认 7 天
     */
    private Long refreshExpiration = 604800000L;

    /**
     * Token 前缀
     */
    private String tokenPrefix = "Bearer ";

    /**
     * Header 名称
     */
    private String headerName = "Authorization";
}


