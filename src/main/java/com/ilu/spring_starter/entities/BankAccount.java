package com.ilu.spring_starter.entities;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ilu.spring_starter.dto.responses.BankAccountResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("deprecation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE bank_accounts SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "account_pin")
    @Min(value = 100000)
    @Max(value = 999999)
    private Integer accountPin;

    @Column(name = "balance")
    @Min(value = 0)
    private double balance;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // @OneToMany(mappedBy = "fromBankAccount")
    // private List<Transaction> fromTransactions;

    // @OneToMany(mappedBy = "toBankAccount")
    // private List<Transaction> toTransactions;

    public BankAccountResponse toResponse() {
        return BankAccountResponse.builder()
                .id(id)
                .accountNumber(accountNumber)
                .accountType(accountType)
                .accountPin(accountPin)
                .balance(balance)
                .user(user.toResponse())
                // .transactions(transactions.stream().map(Transaction::toResponse).toList())
                .createdAt(createdAt == null ? null : Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant()))
                .updatedAt(updatedAt == null ? null : Date.from(updatedAt.atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }
}