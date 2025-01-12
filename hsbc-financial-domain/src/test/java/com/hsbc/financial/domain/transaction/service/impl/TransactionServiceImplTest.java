package com.hsbc.financial.domain.transaction.service.impl;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.domain.transaction.entity.*;
import java.math.BigDecimal;
import java.util.Optional;

import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import com.hsbc.financial.domain.transaction.facade.TransactionEventFacadeService;
import com.hsbc.financial.domain.transaction.event.EventBus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;


/**
 * This class is used for unit testing the TransactionServiceImpl class
 * @author: zhaoyongping8
 * @date: 2025-01-12
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

	@Mock
	private TransactionEventFacadeService transactionEventFacadeService;

	@Mock
	private EventBus eventBus;

	@InjectMocks
	private TransactionServiceImpl transactionServiceImpl;

    @Test
    public void testConstructor() {
        // generated by JoyCoder taskId d728a0fac2c1
        EventBus eventBus = new EventBus() {
            @Override
            public void publish(TransactionEvent event) {

            }
        };
        TransactionEventFacadeService transactionEventFacadeService = new TransactionEventFacadeService() {
            @Override
            public void save(TransactionEvent transactionEvent) {

            }
        };
        AccountFacadeService accountFacadeService = new AccountFacadeService() {
            @Override
            public Optional<Account> findByAccountIdForUpdate(String accountId) {
                return Optional.empty();
            }

            @Override
            public void save(Account account) {

            }

            @Override
            public Optional<Account> findByAccountId(String accountId) {
                return Optional.empty();
            }
        };
        
        TransactionServiceImpl transactionServiceImpl = new TransactionServiceImpl(eventBus, transactionEventFacadeService, accountFacadeService);
        
        assertNotNull(transactionServiceImpl);
    }

    @Test
    public void testProcessTransaction() {
        TransactionCommand command = new TransactionCommand();
        command.setTransactionId("12345");
        command.setSourceAccountId("67890");
        command.setDestAccountId("54321");
        command.setAmount(BigDecimal.TEN);

        doNothing().when(transactionEventFacadeService).save(any(TransactionEvent.class));
        doNothing().when(eventBus).publish(any(TransactionEvent.class));
        transactionServiceImpl.processTransaction(command);
    }

}