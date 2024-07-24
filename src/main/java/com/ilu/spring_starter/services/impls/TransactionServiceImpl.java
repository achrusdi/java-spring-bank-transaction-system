package com.ilu.spring_starter.services.impls;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ilu.spring_starter.entities.BankAccount;
import com.ilu.spring_starter.entities.Transaction;
import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.repositories.TransactionRepository;
import com.ilu.spring_starter.services.BankAccountService;
import com.ilu.spring_starter.services.TransactionService;
import com.ilu.spring_starter.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;
    private final UserService userService;

    @Override
    public boolean deleteById(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> findByDateRange(Date startDate, Date endDate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Transaction> findByDateRangeAndUser(Date startDate, Date endDate, String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Transaction findById(String id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> findByUser(String userId) {
        System.out.println("=========================================");
        User user = userService.findById(userId);
        System.out.println("User" + user);
        System.out.println("=========================================");
        if (user == null) {
            return null;
        }


        List<String> bankAccountNumbers = user.getBankAccounts().stream().map(BankAccount::getAccountNumber).toList();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(transactionRepository.findByFromBankAccountIn(bankAccountNumbers));
        transactions.addAll(transactionRepository.findByToBankAccountIn(bankAccountNumbers));
        transactions.sort(Comparator.comparing(Transaction::getCreatedAt));

        return null;
    }

    @Override
    public Transaction transfer(Transaction transaction) {

        BankAccount from = bankAccountService.findByAccountNumber(transaction.getFromBankAccount().getAccountNumber());
        BankAccount to = bankAccountService.findByAccountNumber(transaction.getToBankAccount().getAccountNumber());

        from.setBalance(from.getBalance() - transaction.getAmount());
        to.setBalance(to.getBalance() + transaction.getAmount());

        bankAccountService.update(from);
        bankAccountService.update(to);

        return transactionRepository.save(transaction);
    }

}