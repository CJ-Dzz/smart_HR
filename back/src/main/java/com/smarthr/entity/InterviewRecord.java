/**
 * 面试题记录实体类
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
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interview_records")
public class InterviewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(length = 20)
    private String difficulty;

    @Column(name = "question_type", length = 20)
    private String questionType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> questions;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
