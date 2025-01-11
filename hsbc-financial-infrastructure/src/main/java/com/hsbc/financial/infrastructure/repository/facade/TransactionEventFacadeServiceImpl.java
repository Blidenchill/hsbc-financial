package com.hsbc.financial.infrastructure.repository.facade;

import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.facade.TransactionEventFacadeService;
import com.hsbc.financial.infrastructure.repository.TransactionEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TransactionEventFacaseServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionEventFacaseServiceImpl
 **/
@Service
public class TransactionEventFacadeServiceImpl implements TransactionEventFacadeService {
    @Autowired
    TransactionEventRepository transactionEventRepository;
    @Override
    public void save(TransactionEvent transactionEvent) {
        transactionEventRepository.save(transactionEvent);
    }
}
