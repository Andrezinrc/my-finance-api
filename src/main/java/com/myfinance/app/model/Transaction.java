package com.myfinance.app.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
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
}