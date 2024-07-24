package com.ilu.spring_starter.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilu.spring_starter.apis.ApiResponse;
import com.ilu.spring_starter.configs.securities.JwtSecurity;
import com.ilu.spring_starter.dto.requests.TransactionRequest;
import com.ilu.spring_starter.dto.responses.TransactionResponse;
import com.ilu.spring_starter.entities.BankAccount;
import com.ilu.spring_starter.entities.Transaction;
import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.services.BankAccountService;
import com.ilu.spring_starter.services.TransactionService;
import com.ilu.spring_starter.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final UserService userService;
    private final JwtSecurity jwtSecurity;

    @GetMapping
    public ApiResponse<List<TransactionResponse>> findAll() {
        List<Transaction> transactions = transactionService.findAll();
        if (transactions == null) {
            return ApiResponse.ok("Transactions not found", List.of());
        }
        return ApiResponse.ok("Transactions fetched successfully", transactions.stream().map(Transaction::toResponse).toList());
    }

    @PostMapping
    public ApiResponse<TransactionResponse> create(HttpServletRequest hsRequest, @RequestBody TransactionRequest request) {
        String userId = jwtSecurity.getUserIdFromRequest(hsRequest);

        if (userId == null) {
            return ApiResponse.badRequest("User not found");
        }

        BankAccount from = bankAccountService.findByAccountNumber(request.getFromAccountNumber());
        BankAccount to = bankAccountService.findByAccountNumber(request.getToAccountNumber());

        Transaction transaction = Transaction.builder()
                .fromBankAccount(from)
                .toBankAccount(to)
                .amount(request.getAmount())
                .build();

        Transaction createdTransaction = transactionService.transfer(transaction);

        if (createdTransaction == null) {
            return ApiResponse.error("Transaction not created");
        }

        return ApiResponse.created("Transaction created successfully", createdTransaction.toResponse());
    }

    @GetMapping("/{id}")
    public ApiResponse<TransactionResponse> findById(HttpServletRequest hsRequest, @PathVariable String id) {
        Transaction transaction = transactionService.findById(id);

        if (transaction == null) {
            return ApiResponse.error("Transaction not found");
        }
        
        String userIdFrom = transaction.getFromBankAccount().getUser().getId();

        String userIdTo = transaction.getToBankAccount().getUser().getId();

        String userIdFromRequest = jwtSecurity.getUserIdFromRequest(hsRequest);

        if (userIdFromRequest == null) {
            return ApiResponse.badRequest("User not found");
        }

        if (userIdFrom == null && userIdTo == null) {
            return ApiResponse.badRequest("Bank account not found");
        }
        
        if (!userIdFromRequest.equals(userIdTo) && !userIdFromRequest.equals(userIdFrom)) {
            return ApiResponse.badRequest("Transaction not found");
        }

        return ApiResponse.ok("Transaction fetched successfully", transaction.toResponse());
    }

    @GetMapping("/user/all")
    public ApiResponse<List<TransactionResponse>> findByUser(HttpServletRequest hsRequest) {
        String userId = jwtSecurity.getUserIdFromRequest(hsRequest);
        if (userId == null) {
            return ApiResponse.badRequest("User not found");
        }
        
        List<Transaction> transactions = transactionService.findByUser(userId);
        if (transactions == null) {
            return ApiResponse.ok("Transactions not found", List.of());
        }
        return ApiResponse.ok("Transactions fetched successfully", transactions.stream().map(Transaction::toResponse).toList());
    }
}