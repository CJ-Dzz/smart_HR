/**
 * 匹配记录实体类
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "match_records")
public class MatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resume_id", nullable = false)
    private Long resumeId;

    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Column(name = "final_score")
    private Float finalScore;

    @Column(name = "rag_score")
    private Float ragScore;

    @Column(name = "graph_score")
    private Float graphScore;

    @Column(name = "llm_score")
    private Float llmScore;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "matched_skills", columnDefinition = "TEXT[]")
    private List<String> matchedSkills;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "missing_skills", columnDefinition = "TEXT[]")
    private List<String> missingSkills;

    @Column(name = "llm_report", columnDefinition = "TEXT")
    private String llmReport;

    @Column(name = "matched_by")
    private Long matchedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
