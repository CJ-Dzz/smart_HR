/**
 * 匹配分析服务
 *
 * @author QinFeng Luo
 * @date 2026/01/12
 */
package com.smarthr.service.hr;

import com.smarthr.dto.match.MatchRequestDTO;
import com.smarthr.dto.match.MatchResultDTO;
import com.smarthr.entity.MatchRecord;
import com.smarthr.entity.Position;
import com.smarthr.entity.Resume;
import com.smarthr.exception.BusinessException;
import com.smarthr.repository.MatchRecordRepository;
import com.smarthr.repository.PositionRepository;
import com.smarthr.repository.ResumeRepository;
import com.smarthr.service.match.HybridMatchService;
import com.smarthr.service.match.MatchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final HybridMatchService hybridMatchService;
    private final ResumeRepository resumeRepository;
    private final PositionRepository positionRepository;
    private final MatchRecordRepository matchRecordRepository;

    /**
     * 执行单次匹配
     */
    @Transactional
    public MatchResultDTO match(MatchRequestDTO request, Long userId) {
        Resume resume = resumeRepository.findById(request.getResumeId())
                .orElseThrow(() -> new BusinessException("简历不存在"));
        Position position = positionRepository.findByIdAndDeletedFalse(request.getPositionId())
                .orElseThrow(() -> new BusinessException("岗位不存在或已删除"));

        // 执行混合匹配
        MatchResult result = hybridMatchService.match(
                resume.getContent(),
                resume.getExtractedSkills(),
                buildPositionContent(position),
                position.getSkills(),
                userId
        );

        // 保存匹配记录
        MatchRecord record = saveMatchRecord(resume, position, result, userId);

        return toDTO(record, result, resume.getFileName(), position.getTitle());
    }

    /**
     * 为简历匹配多个岗位
     */
    @Transactional
    public List<MatchResultDTO> matchResumeToPositions(Long resumeId, Long userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new BusinessException("简历不存在"));
        List<Position> positions = positionRepository.findByDeletedFalse();

        List<MatchResultDTO> results = new ArrayList<>();
        for (Position position : positions) {
            MatchResult result = hybridMatchService.match(
                    resume.getContent(),
                    resume.getExtractedSkills(),
                    buildPositionContent(position),
                    position.getSkills(),
                    userId
            );
            MatchRecord record = saveMatchRecord(resume, position, result, userId);
            results.add(toDTO(record, result, resume.getFileName(), position.getTitle()));
        }

        // 按分数排序
        results.sort((a, b) -> Float.compare(b.getFinalScore(), a.getFinalScore()));
        return results;
    }

    /**
     * 为岗位匹配多份简历
     */
    @Transactional
    public List<MatchResultDTO> matchPositionToResumes(Long positionId, Long userId) {
        Position position = positionRepository.findByIdAndDeletedFalse(positionId)
                .orElseThrow(() -> new BusinessException("岗位不存在或已删除"));
        List<Resume> resumes = resumeRepository.findAll();

        List<MatchResultDTO> results = new ArrayList<>();
        for (Resume resume : resumes) {
            MatchResult result = hybridMatchService.match(
                    resume.getContent(),
                    resume.getExtractedSkills(),
                    buildPositionContent(position),
                    position.getSkills(),
                    userId
            );
            MatchRecord record = saveMatchRecord(resume, position, result, userId);
            results.add(toDTO(record, result, resume.getFileName(), position.getTitle()));
        }

        // 按分数排序
        results.sort((a, b) -> Float.compare(b.getFinalScore(), a.getFinalScore()));
        return results;
    }

    /**
     * 获取匹配记录详情
     */
    public MatchResultDTO getMatchRecord(Long id) {
        MatchRecord record = matchRecordRepository.findById(id)
                .orElseThrow(() -> new BusinessException("匹配记录不存在"));
        
        Resume resume = resumeRepository.findById(record.getResumeId())
                .orElse(null);
        Position position = positionRepository.findById(record.getPositionId())
                .orElse(null);

        return toDTO(record, resume, position);
    }

    /**
     * 分页查询匹配记录
     */
    public Page<MatchResultDTO> listMatchRecords(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<MatchRecord> records = matchRecordRepository.findAll(pageable);
        
        return records.map(record -> {
            Resume resume = resumeRepository.findById(record.getResumeId()).orElse(null);
            Position position = positionRepository.findById(record.getPositionId()).orElse(null);
            return toDTO(record, resume, position);
        });
    }

    /**
     * 获取简历的匹配历史
     */
    public List<MatchResultDTO> getResumeMatchHistory(Long resumeId) {
        List<MatchRecord> records = matchRecordRepository.findByResumeId(resumeId);
        return records.stream()
                .map(record -> {
                    Position position = positionRepository.findById(record.getPositionId()).orElse(null);
                    Resume resume = resumeRepository.findById(record.getResumeId()).orElse(null);
                    return toDTO(record, resume, position);
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取岗位的匹配历史
     */
    public List<MatchResultDTO> getPositionMatchHistory(Long positionId) {
        List<MatchRecord> records = matchRecordRepository.findByPositionId(positionId);
        return records.stream()
                .map(record -> {
                    Resume resume = resumeRepository.findById(record.getResumeId()).orElse(null);
                    Position position = positionRepository.findById(record.getPositionId()).orElse(null);
                    return toDTO(record, resume, position);
                })
                .collect(Collectors.toList());
    }

    /**
     * 保存匹配记录
     */
    private MatchRecord saveMatchRecord(Resume resume, Position position, MatchResult result, Long userId) {
        MatchRecord record = MatchRecord.builder()
                .resumeId(resume.getId())
                .positionId(position.getId())
                .finalScore(result.getFinalScore())
                .ragScore(result.getRagScore())
                .graphScore(result.getGraphScore())
                .llmScore(result.getLlmScore())
                .matchedSkills(result.getMatchedSkills())
                .missingSkills(result.getMissingSkills())
                .llmReport(result.getLlmReport())
                .matchedBy(userId)
                .createdAt(LocalDateTime.now())
                .build();

        return matchRecordRepository.save(record);
    }

    /**
     * 构建岗位内容
     */
    private String buildPositionContent(Position position) {
        StringBuilder sb = new StringBuilder();
        sb.append("岗位: ").append(position.getTitle()).append("\n");
        sb.append("公司: ").append(position.getCompany()).append("\n");
        sb.append("经验要求: ").append(position.getExperience()).append("\n");
        sb.append("学历要求: ").append(position.getEducation()).append("\n");
        sb.append("岗位职责: ").append(position.getResponsibilities()).append("\n");
        sb.append("任职要求: ").append(position.getRequirements()).append("\n");
        if (position.getSkills() != null) {
            sb.append("技能要求: ").append(String.join(", ", position.getSkills()));
        }
        return sb.toString();
    }

    /**
     * 转换为 DTO
     */
    private MatchResultDTO toDTO(MatchRecord record, MatchResult result, String resumeName, String positionTitle) {
        Map<String, Object> scoreDetails = new HashMap<>();
        // RAG 已禁用，权重调整为知识图谱 + LLM 双维度
        // scoreDetails.put("ragWeight", 0.4);
        scoreDetails.put("graphWeight", 0.5);
        scoreDetails.put("llmWeight", 0.5);

        return MatchResultDTO.builder()
                .id(record.getId())
                .resumeId(record.getResumeId())
                .positionId(record.getPositionId())
                .resumeName(resumeName)
                .positionTitle(positionTitle)
                .finalScore(result.getFinalScore())
                .ragScore(result.getRagScore())
                .graphScore(result.getGraphScore())
                .llmScore(result.getLlmScore())
                .matchedSkills(result.getMatchedSkills())
                .missingSkills(result.getMissingSkills())
                .extraSkills(result.getExtraSkills())
                .llmReport(result.getLlmReport())
                .matchGrade(result.getMatchGrade())
                .recommendLevel(result.getRecommendLevel())
                .scoreDetails(scoreDetails)
                .createdAt(record.getCreatedAt())
                .build();
    }

    /**
     * 从记录转换为 DTO
     */
    private MatchResultDTO toDTO(MatchRecord record, Resume resume, Position position) {
        Map<String, Object> scoreDetails = new HashMap<>();
        // RAG 已禁用，权重调整为知识图谱 + LLM 双维度
        // scoreDetails.put("ragWeight", 0.4);
        scoreDetails.put("graphWeight", 0.5);
        scoreDetails.put("llmWeight", 0.5);

        return MatchResultDTO.builder()
                .id(record.getId())
                .resumeId(record.getResumeId())
                .positionId(record.getPositionId())
                .resumeName(resume != null ? resume.getFileName() : "未知")
                .positionTitle(position != null ? position.getTitle() : "未知")
                .finalScore(record.getFinalScore())
                .ragScore(record.getRagScore())
                .graphScore(record.getGraphScore())
                .llmScore(record.getLlmScore())
                .matchedSkills(record.getMatchedSkills())
                .missingSkills(record.getMissingSkills())
                .llmReport(record.getLlmReport())
                .matchGrade(calculateGrade(record.getFinalScore()))
                .recommendLevel(calculateRecommendLevel(record.getFinalScore()))
                .scoreDetails(scoreDetails)
                .createdAt(record.getCreatedAt())
                .build();
    }

    /**
     * 计算匹配等级
     */
    private String calculateGrade(Float score) {
        if (score == null) return "D";
        if (score >= 85) return "A";
        if (score >= 70) return "B";
        if (score >= 50) return "C";
        return "D";
    }

    /**
     * 计算推荐等级
     */
    private Integer calculateRecommendLevel(Float score) {
        if (score == null) return 1;
        if (score >= 90) return 5;
        if (score >= 75) return 4;
        if (score >= 60) return 3;
        if (score >= 45) return 2;
        return 1;
    }
}
