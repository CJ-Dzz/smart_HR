/**
 * 岗位管理服务
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.service.hr;

import com.smarthr.dto.position.PositionCreateRequest;
import com.smarthr.dto.position.PositionDTO;
import com.smarthr.entity.Position;
import com.smarthr.exception.BusinessException;
import com.smarthr.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;

    /**
     * 创建岗位
     */
    @Transactional
    public PositionDTO createPosition(PositionCreateRequest request, Long createdBy) {
        Position position = Position.builder()
                .title(request.getTitle())
                .company(request.getCompany())
                .salaryRange(request.getSalaryRange())
                .experience(request.getExperience())
                .education(request.getEducation())
                .location(request.getLocation())
                .responsibilities(request.getResponsibilities())
                .requirements(request.getRequirements())
                .skills(request.getSkills())
                .createdBy(createdBy)
                .createdAt(LocalDateTime.now())
                .build();

        Position saved = positionRepository.save(position);
        log.info("Created position: {} by user {}", saved.getId(), createdBy);

        return toDTO(saved);
    }

    /**
     * 更新岗位
     */
    @Transactional
    public PositionDTO updatePosition(Long id, PositionCreateRequest request, Long userId) {
        Position position = positionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("岗位不存在"));

        position.setTitle(request.getTitle());
        position.setCompany(request.getCompany());
        position.setSalaryRange(request.getSalaryRange());
        position.setExperience(request.getExperience());
        position.setEducation(request.getEducation());
        position.setLocation(request.getLocation());
        position.setResponsibilities(request.getResponsibilities());
        position.setRequirements(request.getRequirements());
        position.setSkills(request.getSkills());
        position.setUpdatedAt(LocalDateTime.now());

        Position saved = positionRepository.save(position);
        log.info("Updated position: {} by user {}", id, userId);

        return toDTO(saved);
    }

    /**
     * 删除岗位
     */
    @Transactional
    public void deletePosition(Long id, Long userId) {
        Position position = positionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("岗位不存在或已删除"));

        position.setDeleted(true);
        position.setUpdatedAt(LocalDateTime.now());
        positionRepository.save(position);
        log.info("Soft deleted position: {} by user {}", id, userId);
    }

    /**
     * 获取岗位详情
     */
    public PositionDTO getPosition(Long id) {
        Position position = positionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("岗位不存在或已删除"));
        return toDTO(position);
    }

    /**
     * 分页查询岗位列表
     */
    public Page<PositionDTO> listPositions(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Position> positions;
        if (keyword != null && !keyword.isEmpty()) {
            positions = positionRepository.findByTitleContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
        } else {
            positions = positionRepository.findByDeletedFalse(pageable);
        }

        return positions.map(this::toDTO);
    }

    /**
     * 获取所有岗位（用于匹配）
     */
    public List<PositionDTO> getAllPositions() {
        return positionRepository.findByDeletedFalse(Pageable.unpaged()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为 DTO
     */
    private PositionDTO toDTO(Position position) {
        return PositionDTO.builder()
                .id(position.getId())
                .title(position.getTitle())
                .company(position.getCompany())
                .salaryRange(position.getSalaryRange())
                .experience(position.getExperience())
                .education(position.getEducation())
                .location(position.getLocation())
                .responsibilities(position.getResponsibilities())
                .requirements(position.getRequirements())
                .skills(position.getSkills())
                .createdBy(position.getCreatedBy())
                .createdAt(position.getCreatedAt())
                .updatedAt(position.getUpdatedAt())
                .build();
    }
}
