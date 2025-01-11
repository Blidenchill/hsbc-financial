package com.hsbc.financial.infrastructure.repository.facade;

import com.hsbc.financial.domain.account.entity.Account;
import com.hsbc.financial.domain.account.facade.AccountFacadeService;
import com.hsbc.financial.infrastructure.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AccountFacadeServiceImpl
 *
 * @author zhaoyongping
 * @date 2025/1/11
 * @className AccountFacadeServiceImpl
 **/
@Service
public class AccountFacadeServiceImpl implements AccountFacadeService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account findByIdForUpdate(String accountId) {
        return accountRepository.findByAccountIdForUpdate(accountId);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }
}
