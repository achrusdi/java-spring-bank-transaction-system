package com.ilu.spring_starter.services;

import java.util.Date;
import java.util.List;

import com.ilu.spring_starter.entities.Transaction;

public interface TransactionService {
    Transaction transfer(Transaction transaction);

    List<Transaction> findAll();
    
    Transaction findById(String id);

    boolean deleteById(String id);

    List<Transaction> findByUser(String userId);

    List<Transaction> findByDateRange(Date startDate, Date endDate);

    List<Transaction> findByDateRangeAndUser(Date startDate, Date endDate, String userId);
}