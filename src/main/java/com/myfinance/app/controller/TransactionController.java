package com.myfinance.app.controller;

import com.myfinance.app.dto.TransactionDTO;
import com.myfinance.app.model.Transaction;
import com.myfinance.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.io.ByteArrayOutputStream;

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
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    // Busca uma transacao pelo id
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    // Deleta uma transacao pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    // Exporta transacoes como csv
    @GetMapping("/export")
    public ResponseEntity<Resource> exportTransactions() {
       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
       transactionService.exportTransactionsToCsv(outputStream);
    
       ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
    
       return ResponseEntity.ok()
               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
               .contentType(MediaType.parseMediaType("text/csv"))
              .body(resource);
    }
}