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
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createTransaction(@RequestBody @Valid TransactionDTO dto) {
        try {
            Transaction savedTransaction = transactionService.createTransaction(dto);
            System.out.println("Transação criada com sucesso: " + savedTransaction);
            return ResponseEntity.status(HttpStatus.CREATED).body("Transação criada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar transação");
        }
    }




    // Busca todas as transacoes
    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            if(transactions != null && !transactions.isEmpty()){
                return ResponseEntity.ok(transactions);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Ainda não há transações");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar transações");
        }
    }


    

    // Busca uma transacao pelo id
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transação não encontrada com ID: " + id);
        }
    }



    
    // Deleta uma transacao pelo id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar transação com ID: " + id);
        }
    }


    

    // Exporta transacoes como csv
    @GetMapping("/export")
    public ResponseEntity<?> exportTransactions() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            transactionService.exportTransactionsToCsv(outputStream);

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao exportar transações para CSV");
        }
    }
}