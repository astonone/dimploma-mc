package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.StatisticalAccounting;
import com.kulygin.musiccloud.repository.StatisticalAccountingRepository;
import com.kulygin.musiccloud.service.StatisticalAccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticalAccountingServiceImpl implements StatisticalAccountingService {
    @Autowired
    private StatisticalAccountingRepository statisticalAccountingRepository;

    @Override
    public void save(StatisticalAccounting statisticalAccounting) {
        statisticalAccountingRepository.save(statisticalAccounting);
    }

    @Override
    public void saveAll(List<StatisticalAccounting> statisticalAccountings) {
        statisticalAccountingRepository.saveAll(statisticalAccountings);
    }

    @Override
    public List<StatisticalAccounting> findAll() {
        return statisticalAccountingRepository.findAll();
    }
}
