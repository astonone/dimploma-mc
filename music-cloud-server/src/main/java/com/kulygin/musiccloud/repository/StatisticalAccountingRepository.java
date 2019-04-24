package com.kulygin.musiccloud.repository;

import com.kulygin.musiccloud.domain.StatisticalAccounting;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticalAccountingRepository extends JpaRepository<StatisticalAccounting, Long> {
    @Cacheable("statistical")
    StatisticalAccounting findByUserIdAndTrackId(Long userId, Long trackId);
}
