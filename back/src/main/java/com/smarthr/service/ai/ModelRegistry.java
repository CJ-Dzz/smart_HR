/**
 * AI 模型注册中心
 * 管理所有注册的模型适配器
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.service.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ModelRegistry {

    private final Map<String, AIModelAdapter> adapters = new HashMap<>();

    /**
     * 注册模型适配器
     */
    public void register(AIModelAdapter adapter) {
        adapters.put(adapter.getModelId(), adapter);
        log.info("Registered AI model adapter: {} ({}), enabled: {}", 
                adapter.getModelName(), adapter.getModelId(), adapter.isEnabled());
    }

    /**
     * 获取指定模型适配器
     */
    public Optional<AIModelAdapter> get(String modelId) {
        return Optional.ofNullable(adapters.get(modelId));
    }

    /**
     * 获取所有注册的模型适配器
     */
    public List<AIModelAdapter> getAll() {
        return adapters.values().stream().toList();
    }

    /**
     * 获取所有启用的模型适配器
     */
    public List<AIModelAdapter> getEnabled() {
        return adapters.values().stream()
                .filter(AIModelAdapter::isEnabled)
                .collect(Collectors.toList());
    }

    /**
     * 检查模型是否存在且启用
     */
    public boolean isEnabled(String modelId) {
        return adapters.containsKey(modelId) && adapters.get(modelId).isEnabled();
    }

    /**
     * 获取所有模型 ID
     */
    public List<String> getAllModelIds() {
        return adapters.keySet().stream().toList();
    }

    /**
     * 获取所有启用的模型 ID
     */
    public List<String> getEnabledModelIds() {
        return adapters.entrySet().stream()
                .filter(e -> e.getValue().isEnabled())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

