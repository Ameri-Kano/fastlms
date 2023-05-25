package com.zerobase.fastlms.member.repository;

import com.zerobase.fastlms.member.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Integer> {
    Optional<LoginHistory> findFirstByUserIdOrderByLoginDateDesc(String userId);

    List<LoginHistory> findAllByUserIdOrderByLoginDateDesc(String userId);
}
