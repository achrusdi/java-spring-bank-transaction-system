package com.ilu.spring_starter.entities;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ilu.spring_starter.dto.responses.TransactionResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("deprecation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity@SQLDelete(sql = "UPDATE roles SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "from_bank_account_id")
    private BankAccount fromBankAccount;

    @ManyToOne
    @JoinColumn(name = "to_bank_account_id")
    private BankAccount toBankAccount;

    @Column(name = "amount")
    private double amount;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant deletedAt;

    public TransactionResponse toResponse() {
        return TransactionResponse.builder()
                .id(id)
                .fromBankAccount(fromBankAccount.toResponse())
                .toBankAccount(toBankAccount.toResponse())
                .amount(amount)
                .createdAt(createdAt == null ? null : Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant()))
                .updatedAt(updatedAt == null ? null : Date.from(updatedAt.atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }
}