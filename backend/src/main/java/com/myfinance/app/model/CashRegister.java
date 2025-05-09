package com.myfinance.app.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CashRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private double initialBalance;
    private double finalBalance;
    private boolean isClosed;

    @OneToMany(mappedBy = "cashRegister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
    
    public CashRegister() {}

    public CashRegister(double initialBalance) {
        this.initialBalance = initialBalance;
        this.openDate = LocalDateTime.now();
        this.isClosed = false;
    }

    public Long getId() { return id; }
    public LocalDateTime getOpenDate() { return openDate; }
    public LocalDateTime getCloseDate() { return closeDate; }
    public double getInitialBalance() { return initialBalance; }
    public double getFinalBalance() { return finalBalance; }
    public boolean isClosed() { return isClosed; }
    public List<Transaction> getTransactions() { return transactions; }

    public void setId(Long id) { this.id = id; }
    public void setOpenDate(LocalDateTime openDate) { this.openDate = openDate; }
    public void setCloseDate(LocalDateTime closeDate) { this.closeDate = closeDate; }
    public void setInitialBalance(double initialBalance) { this.initialBalance = initialBalance; }
    public void setFinalBalance(double finalBalance) { this.finalBalance = finalBalance; }
    public void setClosed(boolean closed) { isClosed = closed; }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setCashRegister(this);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        transaction.setCashRegister(null);
    }

    @Override
    public String toString() {
        return "CashRegister{" +
                "id=" + id +
                ", openDate=" + openDate +
                ", closeDate=" + closeDate +
                ", initialBalance=" + initialBalance +
                ", finalBalance=" + finalBalance +
                ", isClosed=" + isClosed +
                '}';
    }
}