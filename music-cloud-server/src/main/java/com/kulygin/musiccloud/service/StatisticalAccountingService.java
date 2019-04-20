package com.kulygin.musiccloud.service;

import com.kulygin.musiccloud.domain.StatisticalAccounting;

import java.util.List;

public interface StatisticalAccountingService {
    void save(StatisticalAccounting statisticalAccounting);

    void saveAll(List<StatisticalAccounting> statisticalAccountings);

    List<StatisticalAccounting> findAll();

    StatisticalAccounting findByUserAndTrack(Long userId, Long trackId);
}
