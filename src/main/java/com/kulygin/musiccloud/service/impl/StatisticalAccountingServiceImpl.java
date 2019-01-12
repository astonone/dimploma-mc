package com.kulygin.musiccloud.service.impl;

import com.kulygin.musiccloud.domain.StatisticalAccounting;
import com.kulygin.musiccloud.repository.StatisticalAccountingRepository;
import com.kulygin.musiccloud.service.StatisticalAccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticalAccountingServiceImpl implements StatisticalAccountingService {
    @Autowired
    private StatisticalAccountingRepository statisticalAccountingRepository;

    @Override
    public void save(StatisticalAccounting statisticalAccounting) {
        statisticalAccountingRepository.save(statisticalAccounting);
    }
}
