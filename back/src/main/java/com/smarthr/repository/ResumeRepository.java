/**
 * 简历数据访问接口
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.repository;

import com.smarthr.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserId(Long userId);

    Page<Resume> findByUserId(Long userId, Pageable pageable);

    Page<Resume> findByFileNameContainingIgnoreCase(String fileName, Pageable pageable);

    long countByUserId(Long userId);
}
