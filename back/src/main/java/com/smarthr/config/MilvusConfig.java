/**
 * Milvus 配置类
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.config;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "smart-hr.milvus", name = "enabled", havingValue = "true")
public class MilvusConfig {

    private final MilvusProperties milvusProperties;

    @Bean
    public MilvusClientV2 milvusClient() {
        String uri = String.format("http://%s:%d", milvusProperties.getHost(), milvusProperties.getPort());
        
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(uri)
                .dbName(milvusProperties.getDatabaseName())
                .build();

        MilvusClientV2 client = new MilvusClientV2(connectConfig);
        log.info("Milvus client connected to: {}", uri);
        
        return client;
    }
}
