package com.ilu.spring_starter.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private double amount;
}