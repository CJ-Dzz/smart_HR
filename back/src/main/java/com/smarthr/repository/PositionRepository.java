/**
 * 岗位数据访问接口
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.repository;

import com.smarthr.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByIdAndDeletedFalse(Long id);

    Page<Position> findByCreatedByAndDeletedFalse(Long createdBy, Pageable pageable);

    Page<Position> findByTitleContainingIgnoreCaseAndDeletedFalse(String title, Pageable pageable);

    @Query("SELECT p FROM Position p WHERE " +
           "p.deleted = false AND (" +
           "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.company) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Position> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Position> findByDeletedFalse(Pageable pageable);

    List<Position> findByDeletedFalse();

    List<Position> findByIdInAndDeletedFalse(List<Long> ids);
}
