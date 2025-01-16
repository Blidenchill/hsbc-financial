package com.hsbc.financial.domain.account.service.impl;

import java.math.BigDecimal;

import com.hsbc.financial.domain.common.exception.BusinessException;
import com.hsbc.financial.domain.common.exception.InsufficientBalanceException;
import com.hsbc.financial.domain.transaction.command.TransactionCommand;
import java.util.Optional;

import com.hsbc.financial.domain.transaction.service.TransactionService;
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

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.mockito.*;
import org.springframework.boot.web.embedded.netty.NettyWebServer;

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

    @Mock
    private TransactionService transactionService;



    /**
     * 根据账户ID从缓存中获取账户信息。
     */
    @Test
    public void testGetAccountByIdAccountInCache() {
        Account account = new Account();
        account.setAccountId("123");
        when(cacheService.get(any(String.class))).thenReturn(account);
        when(cacheService.generateKey(any(String.class), any(String.class))).thenReturn("key");
        Account result = accountServiceImpl.getAccountById("123");

        assertEquals("123", result.getAccountId());
    }

    /**
     * 测试获取账户ID不在缓存中的情况。
     */
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

    /**
     * 测试当账户ID不存在时，getAccountById方法是否抛出AccountNotFoundException异常。
     * @throws AccountNotFoundException 如果账户ID不存在
     */
    @Test
    public void testGetAccountByIdAccountNotFound() {
        when(cacheService.get(any(String.class))).thenReturn(null);
        when(cacheService.generateKey(any(String.class), any(String.class))).thenReturn("key");
        when(accountFacadeService.findByAccountId(any(String.class))).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> {
            accountServiceImpl.getAccountById("123");
        });


    }

    /**
     * 创建或更新账户快照，若账户不存在则创建新账户。
     */
    @Test
    public void testCreateOrUpdateSnapshotNewAccount() {
        Account account = new Account();
        account.setAccountId("A123");
        account.setBalance(new BigDecimal("100.00"));

        when(accountSnapshotFacadeService.findTopByAccountIdOrderByVersionDesc("A123")).thenReturn(Optional.empty());

        doNothing().when(accountSnapshotFacadeService).save(any(AccountSnapshot.class));

        accountServiceImpl.createOrUpdateSnapshot(account);
    }

    /**
     * 创建或更新账户快照，覆盖已有的快照。
     */
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

    /**
     * 创建或更新账户快照，当最新的快照为空时。
     */
    @Test
    public void testCreateOrUpdateSnapshotNullLatestSnapshot() {
        Account account = new Account();
        account.setAccountId("A123");
        account.setBalance(new BigDecimal("200.00"));

        when(accountSnapshotFacadeService.findTopByAccountIdOrderByVersionDesc("A123")).thenReturn(Optional.empty());

        doNothing().when(accountSnapshotFacadeService).save(any(AccountSnapshot.class));

        accountServiceImpl.createOrUpdateSnapshot(account);
    }

    /**
     * 测试在查找账户快照时抛出InfrastructureException的情况。
     * @throws BusinessException 如果在创建或更新快照时发生错误。
     */
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

    /**
     * 测试更新账户余额，源账户有足够的余额。
     */
    @Test
    public void testUpdateAccountBalancesSufficientBalance() {
        TransactionCommand command = new TransactionCommand();
        command.setTransactionId(UUID.randomUUID().toString());
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(BigDecimal.TEN);

        Account sourceAccount = new Account();
        sourceAccount.setAccountId(command.getSourceAccountId());
        sourceAccount.setBalance(BigDecimal.TEN);

        Account destAccount = new Account();
        destAccount.setAccountId(command.getDestAccountId());
        destAccount.setBalance(BigDecimal.TEN);

        when(accountFacadeService.findByAccountIdForUpdate(command.getSourceAccountId())).thenReturn(Optional.of(sourceAccount));
        when(accountFacadeService.findByAccountIdForUpdate(command.getDestAccountId())).thenReturn(Optional.of(destAccount));
        doNothing().when(transactionService).changeTransactionProcessed(command.getTransactionId());
        // When
        accountServiceImpl.updateAccountBalances(command);
        // Then
        assertEquals(BigDecimal.ZERO, sourceAccount.getBalance());

    }

    @Test
    public void testUpdateAccountBalancesSufficientBalanceExchangeAccountId() {
        TransactionCommand command = new TransactionCommand();
        command.setTransactionId(UUID.randomUUID().toString());
        command.setSourceAccountId("dest456");
        command.setDestAccountId("source123");
        command.setAmount(BigDecimal.TEN);

        Account sourceAccount = new Account();
        sourceAccount.setAccountId(command.getSourceAccountId());
        sourceAccount.setBalance(BigDecimal.TEN);

        Account destAccount = new Account();
        destAccount.setAccountId(command.getDestAccountId());
        destAccount.setBalance(BigDecimal.TEN);

        when(accountFacadeService.findByAccountIdForUpdate(command.getSourceAccountId())).thenReturn(Optional.of(sourceAccount));
        when(accountFacadeService.findByAccountIdForUpdate(command.getDestAccountId())).thenReturn(Optional.of(destAccount));
        doNothing().when(transactionService).changeTransactionProcessed(command.getTransactionId());
        // When
        accountServiceImpl.updateAccountBalances(command);
        // Then
        assertEquals(BigDecimal.ZERO, sourceAccount.getBalance());

    }


    /**
     * 测试在更新账户余额时，源账户不存在的情况。
     * @throws AccountNotFoundException 如果源账户不存在，则抛出此异常。
     */
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

    /**
     * 测试在更新账户余额时目的账户不存在的情况。
     * @throws AccountNotFoundException 如果目的账户不存在。
     */
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

    /**
     * 测试更新账户余额时抛出其他异常的场景。
     * @throws BusinessException 如果在更新账户余额过程中发生业务异常。
     */
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

    @Test
    public void testUpdateAccountBalancesInsufficientBalanceException() {
        TransactionCommand command = new TransactionCommand();
        command.setSourceAccountId("source123");
        command.setDestAccountId("dest456");
        command.setAmount(BigDecimal.TEN);

        Account sourceAccount = new Account();
        sourceAccount.setAccountId("source123");
        sourceAccount.setBalance(new BigDecimal("9.00"));

        Account destAccount = new Account();
        destAccount.setAccountId("dest456");
        destAccount.setBalance(new BigDecimal("9.00"));

        when(accountFacadeService.findByAccountIdForUpdate("source123")).thenReturn(Optional.of(sourceAccount));
        when(accountFacadeService.findByAccountIdForUpdate("dest456")).thenReturn(Optional.of(destAccount));
        assertThrows(InsufficientBalanceException.class, () -> {
            accountServiceImpl.updateAccountBalances(command);
        });

    }

}