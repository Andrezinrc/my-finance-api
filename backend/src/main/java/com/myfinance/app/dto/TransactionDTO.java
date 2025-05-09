package com.myfinance.app.dto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class TransactionDTO {
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

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Boolean getIsIncome() { return isIncome; }
    public void setIsIncome(Boolean isIncome) { this.isIncome = isIncome; }
}