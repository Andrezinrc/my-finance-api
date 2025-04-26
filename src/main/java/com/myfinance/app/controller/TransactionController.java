package com.myfinance.app.controller;

import com.myfinance.app.dto.TransactionDTO;
import com.myfinance.app.model.Transaction;
import com.myfinance.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Cria uma nova transacao
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody @Valid TransactionDTO dto) {
        Transaction savedTransaction = transactionService.createTransaction(dto);
        return ResponseEntity.ok(savedTransaction);
    }

    // Busca todas as transacoes
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // Busca uma transacao pelo id
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    // Deleta uma transacao pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}