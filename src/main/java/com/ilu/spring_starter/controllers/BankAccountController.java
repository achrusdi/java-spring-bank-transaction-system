package com.ilu.spring_starter.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilu.spring_starter.apis.ApiResponse;
import com.ilu.spring_starter.dto.requests.BankAccountRequest;
import com.ilu.spring_starter.dto.responses.BankAccountResponse;
import com.ilu.spring_starter.entities.BankAccount;
import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.services.BankAccountService;
import com.ilu.spring_starter.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final UserService userService;

    @PostMapping("/{generalId}")
    public ApiResponse<BankAccountResponse> create(@RequestBody BankAccountRequest request, @PathVariable String generalId) {
        User user = userService.findById(generalId);
        
        request.setUser(user);
        if (user == null) {
            return ApiResponse.error("User not found"); 
        }

        BankAccount bankAccount = bankAccountService.create(request.toEntity());

        if (bankAccount == null) {
            return ApiResponse.error("Bank account not created");
        }

        return ApiResponse.created("Bank account created successfully", bankAccount.toResponse());
    }

    @GetMapping
    public ApiResponse<List<BankAccountResponse>> findAll() {
        List<BankAccount> bankAccounts = bankAccountService.findAll();

        if (bankAccounts.isEmpty()) {
            return ApiResponse.ok("Bank accounts not found", List.of());
        }

        return ApiResponse.ok("Bank accounts fetched successfully", bankAccounts.stream().map(BankAccount::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<BankAccountResponse> findById(@PathVariable String id) {
        BankAccount bankAccount = bankAccountService.findById(id);
        if (bankAccount == null) {
            return ApiResponse.error("Bank account not found");
        }
        return ApiResponse.ok("Bank account fetched successfully", bankAccount.toResponse());
    }

    @GetMapping("/bank-account-number/{accountNumber}")
    public ApiResponse<BankAccountResponse> findByAccountNumber(@PathVariable String accountNumber) {
        BankAccount bankAccount = bankAccountService.findByAccountNumber(accountNumber);
        if (bankAccount == null) {
            return ApiResponse.error("Bank account not found");
        }
        return ApiResponse.ok("Bank account fetched successfully", bankAccount.toResponse());
    }

    @PutMapping
    public ApiResponse<BankAccountResponse> update(@RequestBody BankAccountRequest request) {
        BankAccount bankAccount = bankAccountService.update(request.toEntity());
        if (bankAccount == null) {
            return ApiResponse.error("Bank account not updated");
        }
        return ApiResponse.ok("Bank account updated successfully", bankAccount.toResponse());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteById(@PathVariable String id) {
        if (bankAccountService.deleteById(id)) {
            return ApiResponse.ok("Bank account deleted successfully");
        }
        return ApiResponse.error("Bank account not deleted");
    }
}