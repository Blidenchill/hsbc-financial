package com.hsbc.financial.infrastructure.repository.facade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.sql.Timestamp;
import com.hsbc.financial.domain.account.entity.Account;

import com.hsbc.financial.infrastructure.repository.AccountRepository;
import java.util.Optional;
import org.springframework.dao.DeadlockLoserDataAccessException;
import com.hsbc.financial.domain.common.exception.InfrastructureException;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
/**
 * This class is used for unit testing the AccountFacadeServiceImpl class.(Generated by JoyCoder)
 * @author: zhaoyongping8
 * @date: 2025-01-12
 */
@ExtendWith(MockitoExtension.class)
public class AccountFacadeServiceImplTest {

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountFacadeServiceImpl accountFacadeServiceImpl;


    @Test
    public void testSaveSuccess() {
        // generated by JoyCoder taskId ba5fd4dbb3b4
        Account account = new Account();
        account.setAccountId("12345");
        account.setAccountName("Test Account");
        account.setBalance(BigDecimal.TEN);
        account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        account.setIsDeleted(false);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountFacadeServiceImpl.save(account);

        // Add assertions if needed
    }

    @Test
    public void testSaveException() {
        // generated by JoyCoder taskId ba5fd4dbb3b4
        Account account = new Account();
        account.setAccountId("54321");
        account.setAccountName("Another Test Account");
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        account.setIsDeleted(false);

        when(accountRepository.save(any(Account.class))).thenThrow(new RuntimeException("Mocked exception"));
        assertThrows(InfrastructureException.class, () -> {
            accountFacadeServiceImpl.save(account);
        });

    }

    @Test
    public void testFindByAccountIdSuccess() {
        // generated by JoyCoder taskId ba5fd4dbb3b4
        // Mock data
        Account account = new Account();
        account.setAccountId("123456");
        account.setAccountName("Test Account");
        account.setBalance(new BigDecimal("1000.00"));
        account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        account.setIsDeleted(false);

        // Mocking external method
        when(accountRepository.findByAccountId(any(String.class))).thenReturn(Optional.of(account));

        // Test
        Optional<Account> result = accountFacadeServiceImpl.findByAccountId("123456");

        // Verify
        assertEquals(account, result.get());
    }

    @Test
    public void testFindByAccountIdException() {
        // generated by JoyCoder taskId ba5fd4dbb3b4
        // Mocking external method to throw an exception
        when(accountRepository.findByAccountId(any(String.class))).thenThrow(new RuntimeException());
        assertThrows(InfrastructureException.class, () -> {
            accountFacadeServiceImpl.findByAccountId("789012");

        });
    }

    @Test
    public void testFindByAccountIdForUpdateSuccess() {
        // generated by JoyCoder taskId ba5fd4dbb3b4
        // Mocking the external method call
        Account account = new Account();
        account.setId(1L);
        account.setAccountId("123456");
        account.setAccountName("Test Account");
        account.setBalance(new BigDecimal("1000.00"));
        account.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        account.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        account.setIsDeleted(false);

        when(accountRepository.findByAccountIdForUpdate(any(String.class))).thenReturn(Optional.of(account));

        // Calling the actual method
        Optional<Account> result = accountFacadeServiceImpl.findByAccountIdForUpdate("123456");

        // Validating the result
        assertEquals(account, result.get());
    }

    @Test
    public void testFindByAccountIdForUpdateDeadlockException() {
        when(accountRepository.findByAccountIdForUpdate(any(String.class))).thenThrow(new DeadlockLoserDataAccessException("Deadlock Exception", null));
        assertThrows(InfrastructureException.class, () -> {
            accountFacadeServiceImpl.findByAccountIdForUpdate("123456");
        });
    }
    @Test
    public void testFindByAccountIdForUpdateOtherException() {
        when(accountRepository.findByAccountIdForUpdate(any(String.class))).thenThrow(new RuntimeException("Some other exception"));
        assertThrows(InfrastructureException.class, () -> {
            accountFacadeServiceImpl.findByAccountIdForUpdate("123456");
        });

    }

}