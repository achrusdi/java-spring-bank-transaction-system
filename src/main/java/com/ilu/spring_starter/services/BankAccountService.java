package com.ilu.spring_starter.services;

import java.util.List;

import com.ilu.spring_starter.entities.BankAccount;

public interface BankAccountService {
    BankAccount create(BankAccount bankAccount);

    BankAccount findById(String id);

    BankAccount update(BankAccount bankAccount);

    List<BankAccount> findAll();

    boolean deleteById(String id);

    BankAccount findByAccountNumber(String accountNumber);
}