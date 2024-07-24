package com.ilu.spring_starter.services.impls;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.ilu.spring_starter.entities.BankAccount;
import com.ilu.spring_starter.repositories.BankAccountRepository;
import com.ilu.spring_starter.services.BankAccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    @Override
    public BankAccount create(BankAccount bankAccount) {
        bankAccount.setAccountNumber(generateBankAccountNumber());
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public boolean deleteById(String id) {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElse(null);
        if (bankAccount == null) {
            return false;
        }
        bankAccountRepository.delete(bankAccount);
        return true;
    }

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public BankAccount findById(String id) {
        return bankAccountRepository.findById(id).orElse(null);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        BankAccount existingBankAccount = bankAccountRepository.findById(bankAccount.getId()).orElse(null);
        if (existingBankAccount == null) {
            return null;
        }

        if (bankAccount.getAccountType() == null) {
            bankAccount.setAccountType(existingBankAccount.getAccountType());
        }

        if (bankAccount.getAccountPin() == null) {
            bankAccount.setAccountPin(existingBankAccount.getAccountPin());
        }

        if (bankAccount.getBalance() <= 0) {
            bankAccount.setBalance(existingBankAccount.getBalance());
        }

        bankAccount.setUser(existingBankAccount.getUser());

        bankAccount.setAccountNumber(existingBankAccount.getAccountNumber());

        bankAccount.setCreatedAt(existingBankAccount.getCreatedAt());

        return bankAccountRepository.save(bankAccount);
    }

    private String generateBankAccountNumber() {
        long min = 1000000000000000L;
        long max = 9999999999999999L;
        long random = ThreadLocalRandom.current().nextLong(min, max + 1);
        return String.valueOf(random);
    }

    
}