/**
 * 匹配记录数据访问接口
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.repository;

import com.smarthr.entity.MatchRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {

    List<MatchRecord> findByResumeId(Long resumeId);

    List<MatchRecord> findByPositionId(Long positionId);

    Page<MatchRecord> findByResumeId(Long resumeId, Pageable pageable);

    Page<MatchRecord> findByPositionId(Long positionId, Pageable pageable);

    Page<MatchRecord> findByMatchedBy(Long matchedBy, Pageable pageable);
}
