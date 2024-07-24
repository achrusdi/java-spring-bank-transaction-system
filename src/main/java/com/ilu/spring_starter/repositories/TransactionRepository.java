package com.ilu.spring_starter.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ilu.spring_starter.entities.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>{
    List<Transaction> findByFromBankAccountIn(List<String> bankAccountIds);
    List<Transaction> findByToBankAccountIn(List<String> bankAccountIds);
}