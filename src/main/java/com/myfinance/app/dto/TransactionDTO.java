package com.myfinance.app.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
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
}