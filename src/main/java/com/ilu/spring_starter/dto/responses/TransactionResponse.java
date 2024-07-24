package com.ilu.spring_starter.dto.responses;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private BankAccountResponse fromBankAccount;
    private BankAccountResponse toBankAccount;
    private double amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d-M-yyyy HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d-M-yyyy HH:mm:ss")
    private Date updatedAt;
}