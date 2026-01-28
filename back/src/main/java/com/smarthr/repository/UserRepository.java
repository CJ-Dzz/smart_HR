/**
 * 用户数据访问接口
 *
 * @author QinFeng Luo
 * @date 2026/01/09
 */
package com.smarthr.repository;

import com.smarthr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}


