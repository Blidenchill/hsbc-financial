package com.hsbc.financial.domain.transaction.service.impl;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.domain.common.exception.BusinessException;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void testProcessTransaction_NullCommand() {
        // given
        TransactionCommand command = null;

        // then
        assertThrows(IllegalArgumentException.class, () -> transactionService.processTransaction(command));
    }

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

    @Test
    public void testChangeTransactionProcessed_Exception() throws Exception {
        // given
        String transactionId = UUID.randomUUID().toString();
        doThrow(new InfrastructureException("Database error")).when(transactionEventFacadeService).updateStatusByTransactionId(anyString(), any(), anyString());

        // then
        assertThrows(BusinessException.class, () -> transactionService.changeTransactionProcessed(transactionId));
    }

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

    @Test
    public void testChangeTransactionFailed_Exception() throws Exception {
        // given
        String transactionId = UUID.randomUUID().toString();
        String failReason = "Insufficient funds";
        doThrow(new InfrastructureException("Database error")).when(transactionEventFacadeService).updateStatusByTransactionId(anyString(), any(), anyString());

        // then
        assertThrows(RuntimeException.class, () -> transactionService.changeTransactionFailed(transactionId, failReason));
    }

}