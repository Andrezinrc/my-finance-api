package com.myfinance.app.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String description;

    @NotNull
    private Double amount;

    @NotNull
    private LocalDateTime date;

    @NotBlank
    private String category;

    @NotNull
    private Boolean isIncome;
    
    @ManyToOne
    @JoinColumn(name = "cash_register_id")
    private CashRegister cashRegister;

    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsIncome() { return isIncome; }
    public void setIsIncome(Boolean isIncome) {
        this.isIncome = isIncome;
    }

    public CashRegister getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }
}