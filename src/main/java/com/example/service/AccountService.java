package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) {        
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null;
        }

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        if (accountRepository.existsByUsername(account.getUsername())) {
            return null;
        }        
        return accountRepository.save(account);
    }

    public Account loginAccount(Account account) {
        return accountRepository.findByUsername(account.getUsername())
            .filter(existingAccount -> existingAccount.getPassword().equals(account.getPassword()))
            .orElse(null);
    }

    public boolean accountExistsById(Integer accountId) {
        return accountRepository.existsById(accountId);
    }

    public boolean usernameExists(String username) {
        return accountRepository.existsByUsername(username);
    }
}
