package com.hsbc.financial.infrastructure.repository.facade;

import com.hsbc.financial.domain.common.exception.InfrastructureException;
import com.hsbc.financial.domain.enums.TransactionStatus;
import com.hsbc.financial.domain.transaction.entity.TransactionEvent;
import com.hsbc.financial.domain.transaction.facade.TransactionEventFacadeService;
import com.hsbc.financial.infrastructure.repository.TransactionEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * TransactionEventFacaseServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className TransactionEventFacaseServiceImpl
 **/
@Service
@Slf4j
public class TransactionEventFacadeServiceImpl implements TransactionEventFacadeService {

    /**
     * 用于管理交易事件的数据访问对象。
     */
    private final TransactionEventRepository transactionEventRepository;

    /**
     * 构造TransactionEventFacadeServiceImpl实例并注入TransactionEventRepository。
     * @param transactionEventRepository TransactionEventRepository对象，用于管理事务事件。
     */
    @Autowired
    public TransactionEventFacadeServiceImpl(TransactionEventRepository transactionEventRepository) {
        this.transactionEventRepository = transactionEventRepository;
    }

    /**
     * 保存交易事件
     * @param transactionEvent 交易事件对象
     */
    @Override
    public void save(TransactionEvent transactionEvent) {
        try{
            transactionEventRepository.save(transactionEvent);
        } catch (Exception ex) {
            log.error("当前数据库保存账号快照信息异常", ex);
            throw new InfrastructureException("当前数据库保存账号快照信息异常");
        }
    }

    /**
     * 根据交易ID更新交易状态。
     * @param transactionId 交易ID。
     * @param status 新的交易状态。
     */
    @Override
    public void updateStatusByTransactionId(String transactionId, TransactionStatus status, String failReason) {
        try{
            int updateNum = transactionEventRepository.updateStatusByTransactionId(transactionId, status, failReason);
            if(updateNum > 0) {
                log.info("根据交易ID更新交易状态成功, transactionId={}, status={}", transactionId, status);
            } else {
                log.warn("未更新成功, 可能transactionId={}不存在", transactionId);
            }

        } catch (Exception ex) {
            log.error("当前数据库更新状态异常", ex);
            throw new InfrastructureException("changeTransactionProcessed");
        }
    }

    @Override
    public Optional<TransactionEvent> findByTransactionId(String transactionId) {
        try{
            return transactionEventRepository.findByTransactionId(transactionId);
        } catch (Exception ex) {
            log.error("当前数据库更新状态异常", ex);
            throw new InfrastructureException("changeTransactionProcessed");
        }
    }
}
