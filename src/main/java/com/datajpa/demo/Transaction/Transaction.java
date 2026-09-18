package com.datajpa.demo.Transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

import java.util.Random;

import java.time.LocalDateTime;


@Entity
@Builder

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String trans_id =
            "TXN-" +
                    System.currentTimeMillis() +
                    "-" +
                    new Random().nextInt(1000);
    private Double trans_amount;
    private LocalDateTime trans_datetime;
    private String transaction_status;
    private LocalDateTime created_at;
    private TransactionType transactionType;

    public Transaction(Integer id, String trans_id, Double trans_amount, LocalDateTime trans_datetime, String transaction_status, LocalDateTime created_at, TransactionType transactionType) {
        this.id = id;
        this.trans_id = "TXN-" +
                System.currentTimeMillis() +
                "-" +
                new Random().nextInt(1000);;
        this.trans_amount = trans_amount;
        this.trans_datetime = trans_datetime;
        this.transaction_status = transaction_status;
        this.created_at = created_at;
        this.transactionType = transactionType;
    }

    public Transaction() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public Double getTrans_amount() {
        return trans_amount;
    }

    public void setTrans_amount(Double trans_amount) {
        this.trans_amount = trans_amount;
    }

    public LocalDateTime getTrans_datetime() {
        return trans_datetime;
    }

    public void setTrans_datetime(LocalDateTime trans_datetime) {
        this.trans_datetime = trans_datetime;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
