package com.ilu.spring_starter.dto.requests;

import com.ilu.spring_starter.entities.BankAccount;
import com.ilu.spring_starter.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountRequest {
    private String id;
    private String accountNumber;
    private String accountType;
    private Integer accountPin;
    private double balance;
    private User user;

    public BankAccount toEntity() {
        return BankAccount.builder()
                .id(id)
                .accountNumber(accountNumber)
                .accountType(accountType)
                .accountPin(accountPin)
                .balance(balance)
                .user(user)
                .build();
    }
}