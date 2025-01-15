package com.hsbc.financial.domain.account.service.impl;

import java.math.BigDecimal;

import com.hsbc.financial.domain.common.exception.BusinessException;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import com.hsbc.financial.domain.account.entity.AccountSnapshot;
import com.hsbc.financial.domain.common.cache.CacheService;
import com.hsbc.financial.domain.account.facade.AccountSnapshotFacadeService;
import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.common.exception.AccountNotFoundException;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import java.util.concurrent.TimeUnit;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * This class is used for unit testing the AccountServiceImpl class.
 * @author: zhaoyongping8
 * @date: 2025-01-12
 */
@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	@Mock
	private CacheService cacheService;

	@Mock
	private AccountSnapshotFacadeService accountSnapshotFacadeService;

	@Mock
	private AccountFacadeService accountFacadeService;



    @Test
    public void testGetAccountByIdAccountInCache() {
        Account account = new Account();
        account.setAccountId("123");
        when(cacheService.get(any(String.class))).thenReturn(account);
        when(cacheService.generateKey(any(String.class), any(String.class))).thenReturn("key");
        Account result = accountServiceImpl.getAccountById("123");

        assertEquals("123", result.getAccountId());
    }

    @Test
    public void testGetAccountByIdAccountNotInCache() {
        Account account = new Account();
        account.setAccountId("123");
        when(cacheService.get(any(String.class))).thenReturn(null);
        when(accountFacadeService.findByAccountId(any(String.class))).thenReturn(Optional.of(account));
        when(cacheService.generateKey(any(String.class), any(String.class))).thenReturn("key");
        doNothing().when(cacheService).add(any(String.class), any(Account.class), any(long.class), any(TimeUnit.class));

        Account result = accountServiceImpl.getAccountById("123");

        assertEquals("123", result.getAccountId());
    }

    @Test
    public void testGetAccountByIdAccountNotFound() {
        when(cacheService.get(any(String.class))).thenReturn(null);
        when(cacheService.generateKey(any(String.class), any(String.class))).thenReturn("key");
        when(accountFacadeService.findByAccountId(any(String.class))).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> {
            accountServiceImpl.getAccountById("123");
        });


    }

    @Test
    public void testCreateOrUpdateSnapshotNewAccount() {
        Account account = new Account();
        account.setAccountId("A123");
        account.setBalance(new BigDecimal("100.00"));

        when(accountSnapshotFacadeService.findTopByAccountIdOrderByVersionDesc("A123")).thenReturn(Optional.empty());

        doNothing().when(accountSnapshotFacadeService).save(any(AccountSnapshot.class));

        accountServiceImpl.createOrUpdateSnapshot(account);
    }

    @Test
    public void testCreateOrUpdateSnapshotExistingAccount() {
        AccountSnapshot latestSnapshot = new AccountSnapshot();
        latestSnapshot.setVersion(2L);

        Account account = new Account();
        account.setAccountId("A123");
        account.setBalance(new BigDecimal("150.00"));

        when(accountSnapshotFacadeService.findTopByAccountIdOrderByVersionDesc("A123")).thenReturn(Optional.of(latestSnapshot));

        doNothing().when(accountSnapshotFacadeService).save(any(AccountSnapshot.class));

        accountServiceImpl.createOrUpdateSnapshot(account);
    }

    @Test
    public void testCreateOrUpdateSnapshotNullLatestSnapshot() {
        Account account = new Account();
        account.setAccountId("A123");
        account.setBalance(new BigDecimal("200.00"));

        when(accountSnapshotFacadeService.findTopByAccountIdOrderByVersionDesc("A123")).thenReturn(Optional.empty());

        doNothing().when(accountSnapshotFacadeService).save(any(AccountSnapshot.class));

        accountServiceImpl.createOrUpdateSnapshot(account);
    }

    @Test
    public void testCreateOrUpdateSnapshotExceptionInFindTopByAccountId() {
        Account account = new Account();
        account.setAccountId("A123");
        account.setBalance(new BigDecimal("250.00"));

        lenient().when(accountSnapshotFacadeService.findTopByAccountIdOrderByVersionDesc("A123")).thenThrow(new InfrastructureException("数据库超时"));

        assertThrows(BusinessException.class, () -> {
            accountServiceImpl.createOrUpdateSnapshot(account);
        });

    }

    @Test
    public void testUpdateAccountBalancesSufficientBalance() {
        TransactionCommand command = new TransactionCommand();
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(BigDecimal.TEN);

        Account sourceAccount = new Account();
        sourceAccount.setAccountId("source123");
        sourceAccount.setBalance(BigDecimal.TEN);

        Account destAccount = new Account();
        destAccount.setAccountId("dest456");
        destAccount.setBalance(BigDecimal.TEN);

        when(accountFacadeService.findByAccountIdForUpdate("source123")).thenReturn(Optional.of(sourceAccount));
        when(accountFacadeService.findByAccountIdForUpdate("dest456")).thenReturn(Optional.of(destAccount));
        // When
        accountServiceImpl.updateAccountBalances(command);
        // Then
        assertEquals(BigDecimal.ZERO, sourceAccount.getBalance());

    }


    @Test
    public void testUpdateAccountBalancesSourceAccountNotFound() {
        TransactionCommand command = new TransactionCommand();
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(BigDecimal.TEN);

        when(accountFacadeService.findByAccountIdForUpdate("dest456")).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> {
            accountServiceImpl.updateAccountBalances(command);
        });
    }

    @Test
    public void testUpdateAccountBalancesDestAccountNotFound() {
        TransactionCommand command = new TransactionCommand();
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(BigDecimal.TEN);

        Account sourceAccount = new Account();
        sourceAccount.setAccountId("source123");

        lenient().when(accountFacadeService.findByAccountIdForUpdate("source123")).thenReturn(Optional.of(sourceAccount));
        lenient().when(accountFacadeService.findByAccountIdForUpdate("dest456")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountServiceImpl.updateAccountBalances(command);
        });
    }

    @Test
    public void testUpdateAccountBalancesDeadlockException() {
        TransactionCommand command = new TransactionCommand();
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(BigDecimal.TEN);

        when(accountFacadeService.findByAccountIdForUpdate("source123")).thenThrow(new InfrastructureException("Deadlock", null));

        assertThrows(BusinessException.class, () -> {
            accountServiceImpl.updateAccountBalances(command);
        });
    }

    @Test
    public void testUpdateAccountBalancesOtherException() {
        TransactionCommand command = new TransactionCommand();
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(BigDecimal.TEN);

        when(accountFacadeService.findByAccountIdForUpdate("source123")).thenThrow(new RuntimeException("Some error"));
        assertThrows(BusinessException.class, () -> {
            accountServiceImpl.updateAccountBalances(command);
        });
    }

}