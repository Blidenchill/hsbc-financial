package com.hsbc.financial.domain.transaction.service.impl;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.domain.common.exception.BusinessException;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import com.hsbc.financial.domain.common.exception.TransactionNotFoundException;
import com.hsbc.financial.domain.common.utils.JacksonUtil;
import com.hsbc.financial.domain.enums.TransactionStatus;
import com.hsbc.financial.domain.transaction.entity.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.facade.TransactionEventFacadeService;
import com.hsbc.financial.domain.transaction.event.EventBus;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * This class is used for unit testing the TransactionServiceImpl class
 * @author: zhaoyongping8
 * @date: 2025-01-12
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {
    @Mock
    private EventBus eventBus;

    @Mock
    private TransactionEventFacadeService transactionEventFacadeService;

    @Mock
    private AccountFacadeService accountFacadeService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    /**
     * 测试交易处理成功情况。
     */
    @Test
    public void testProcessTransaction_Success() {
        // given
        String sourceAccountId = "sourceAccountId";
        String destAccountId = "destAccountId";
        BigDecimal amount = BigDecimal.valueOf(100);

        TransactionCommand command = new TransactionCommand();
        command.setTransactionId(UUID.randomUUID().toString());
        command.setSourceAccountId(sourceAccountId);
        command.setDestAccountId(destAccountId);
        command.setAmount(amount);

        Account sourceAccount = new Account();
        sourceAccount.setAccountId(sourceAccountId);
        sourceAccount.setId(1L);
        when(accountFacadeService.findByAccountId(sourceAccountId)).thenReturn(Optional.of(sourceAccount));

        Account destAccount = new Account();
        destAccount.setAccountId(destAccountId);
        when(accountFacadeService.findByAccountId(destAccountId)).thenReturn(Optional.of(destAccount));

        // when
        transactionService.processTransaction(command);

        // then
        ArgumentCaptor<TransactionEvent> eventCaptor = ArgumentCaptor.forClass(TransactionEvent.class);
        verify(transactionEventFacadeService, times(1)).save(eventCaptor.capture());
        verify(eventBus, times(1)).publish(eventCaptor.capture());

        TransactionEvent event = eventCaptor.getValue();
        assertNotNull(event.getEventId());
        assertEquals(command.getTransactionId(), event.getTransactionId());
        assertEquals(sourceAccountId, event.getSourceAccountId());
        assertEquals(destAccountId, event.getDestAccountId());
        assertEquals(amount, event.getAmount());
        assertEquals("TRANSACTION_CREATED", event.getEventType());
        assertEquals(TransactionStatus.SUBMITTED, event.getStatus());
        assertEquals(JacksonUtil.toJson(command), event.getEventData());
        assertNotNull(event.getCreatedAt());
    }

    /**
     * 测试 processTransaction 方法在传入 null 命令时抛出 IllegalArgumentException 异常。
     * @throws IllegalArgumentException 当传入的命令为 null 时抛出此异常。
     */
    @Test
    public void testProcessTransaction_NullCommand() {
        // given
        TransactionCommand command = null;

        // then
        assertThrows(IllegalArgumentException.class, () -> transactionService.processTransaction(command));
    }

    /**
     * 测试processTransaction方法处理空源账户ID的场景。
     * @throws IllegalArgumentException 如果源账户ID为空。
     */
    @Test
    public void testProcessTransaction_NullAccountId() {
        // given
        TransactionCommand command = new TransactionCommand();
        command.setTransactionId(UUID.randomUUID().toString());
        command.setSourceAccountId(null);
        command.setDestAccountId(null);
        command.setAmount(BigDecimal.valueOf(100));

        // then
        assertThrows(IllegalArgumentException.class, () -> transactionService.processTransaction(command));
    }

    /**
     * 测试同一账户ID的交易命令处理是否抛出IllegalArgumentException异常。
     */
    @Test
    public void testProcessTransaction_SameAccountId() {
        // given
        String accountId = "accountId";
        TransactionCommand command = new TransactionCommand();
        command.setTransactionId(UUID.randomUUID().toString());
        command.setSourceAccountId(accountId);
        command.setDestAccountId(accountId);
        command.setAmount(BigDecimal.valueOf(100));

        // then
        assertThrows(IllegalArgumentException.class, () -> transactionService.processTransaction(command));
    }

    /**
     * 测试在源账户不存在的情况下调用 processTransaction 方法是否抛出 IllegalArgumentException 异常。
     */
    @Test
    public void testProcessTransaction_SourceAccountNotFound() {
        // given
        String sourceAccountId = "sourceAccountId";
        String destAccountId = "destAccountId";
        TransactionCommand command = new TransactionCommand();
        command.setTransactionId(UUID.randomUUID().toString());
        command.setSourceAccountId(sourceAccountId);
        command.setDestAccountId(destAccountId);
        command.setAmount(BigDecimal.valueOf(100));

        lenient().when(accountFacadeService.findByAccountId(sourceAccountId)).thenReturn(Optional.empty());
        lenient().when(accountFacadeService.findByAccountId(destAccountId)).thenReturn(Optional.of(new Account()));

        // then
        assertThrows(IllegalArgumentException.class, () -> transactionService.processTransaction(command));
    }

    /**
     * 测试当目的账户不存在时，processTransaction方法是否抛出IllegalArgumentException异常。
     */
    @Test
    public void testProcessTransaction_DestAccountNotFound() {
        // given
        String sourceAccountId = "sourceAccountId";
        String destAccountId = "destAccountId";
        TransactionCommand command = new TransactionCommand();
        command.setTransactionId(UUID.randomUUID().toString());
        command.setSourceAccountId(sourceAccountId);
        command.setDestAccountId(destAccountId);
        command.setAmount(BigDecimal.valueOf(100));

        when(accountFacadeService.findByAccountId(sourceAccountId)).thenReturn(Optional.of(new Account()));
        when(accountFacadeService.findByAccountId(destAccountId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> transactionService.processTransaction(command));
    }

    /**
     * 测试更改交易处理状态的成功情况。
     */
    @Test
    public void testChangeTransactionProcessed_Success() {
        // given
        String transactionId = UUID.randomUUID().toString();

        // when
        transactionService.changeTransactionProcessed(transactionId);

        // then
        try {
            verify(transactionEventFacadeService, times(1)).updateStatusByTransactionId(transactionId, TransactionStatus.PROCESSED, StringUtils.EMPTY);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * 测试当更新交易状态时抛出 InfrastructureException 异常的场景。
     * @throws BusinessException 如果更新交易状态失败
     */
    @Test
    public void testChangeTransactionProcessed_Exception() throws Exception {
        // given
        String transactionId = UUID.randomUUID().toString();
        doThrow(new InfrastructureException("Database error")).when(transactionEventFacadeService).updateStatusByTransactionId(anyString(), any(), anyString());

        // then
        assertThrows(BusinessException.class, () -> transactionService.changeTransactionProcessed(transactionId));
    }

    /**
     * 测试更改交易状态为失败的场景。
     */
    @Test
    public void testChangeTransactionFailed_Success() {
        // given
        String transactionId = UUID.randomUUID().toString();
        String failReason = "Insufficient funds";

        // when
        transactionService.changeTransactionFailed(transactionId, failReason);

        // then
        try {
            verify(transactionEventFacadeService, times(1)).updateStatusByTransactionId(transactionId, TransactionStatus.FAILED, failReason);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * 测试当更新交易状态失败时，changeTransactionFailed方法是否抛出RuntimeException。
     * @throws Exception 可能抛出的异常
     */
    @Test
    public void testChangeTransactionFailed_Exception() throws Exception {
        // given
        String transactionId = UUID.randomUUID().toString();
        String failReason = "Insufficient funds";
        doThrow(new InfrastructureException("Database error")).when(transactionEventFacadeService).updateStatusByTransactionId(anyString(), any(), anyString());

        // then
        assertThrows(RuntimeException.class, () -> transactionService.changeTransactionFailed(transactionId, failReason));
    }

    /**
     * 检查交易是否已处理。
     */
    @Test
    void testCheckTransactionProcessed_Success() throws Exception {
        String transactionId = "testId";
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setStatus(TransactionStatus.PROCESSED);
        when(transactionEventFacadeService.findByTransactionId(transactionId)).thenReturn(Optional.of(transactionEvent));

        Boolean result = transactionService.checkTransactionProcessed(transactionId);

        assertTrue(result);
        verify(transactionEventFacadeService, times(1)).findByTransactionId(transactionId);
    }

    /**
     * 测试当事务状态为空时，检查事务是否被处理的方法。
     */
    @Test
    void testCheckTransactionProcessed_StatusNull() throws Exception {
        String transactionId = "testId";
        TransactionEvent transactionEvent = new TransactionEvent();
        when(transactionEventFacadeService.findByTransactionId(transactionId)).thenReturn(Optional.of(transactionEvent));

        Boolean result = transactionService.checkTransactionProcessed(transactionId);

        assertFalse(result);
        verify(transactionEventFacadeService, times(1)).findByTransactionId(transactionId);
    }

    /**
     * 检查交易是否已处理。
     */
    @Test
    void testCheckTransactionProcessed_StatusNotProcessed() throws Exception {
        String transactionId = "testId";
        TransactionEvent transactionEvent = new TransactionEvent();
        transactionEvent.setStatus(TransactionStatus.SUBMITTED);
        when(transactionEventFacadeService.findByTransactionId(transactionId)).thenReturn(Optional.of(transactionEvent));

        Boolean result = transactionService.checkTransactionProcessed(transactionId);

        assertFalse(result);
        verify(transactionEventFacadeService, times(1)).findByTransactionId(transactionId);
    }
    /**
     * 测试在查找交易事件时抛出异常的情况。
     * @throws BusinessException 如果在处理交易时发生业务异常。
     */
    @Test
    void testCheckTransactionProcessed_Exception() throws Exception {
        String transactionId = "testId";
        when(transactionEventFacadeService.findByTransactionId(transactionId)).thenThrow(new RuntimeException("Database error"));

        assertThrows(BusinessException.class, () -> transactionService.checkTransactionProcessed(transactionId));
        verify(transactionEventFacadeService, times(1)).findByTransactionId(transactionId);
    }

    /**
     * 测试当事务未找到时，检查事务是否已处理的方法。
     * @throws TransactionNotFoundException 如果事务未找到则抛出此异常
     */
    @Test
    void testCheckTransactionProcessed_TransactionNotFound() throws Exception {
        String transactionId = "testId";
        when(transactionEventFacadeService.findByTransactionId(transactionId)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.checkTransactionProcessed(transactionId));
        verify(transactionEventFacadeService, times(1)).findByTransactionId(transactionId);
    }


}