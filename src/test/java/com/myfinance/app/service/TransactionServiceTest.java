package com.myfinance.app.service;

import com.myfinance.app.dto.TransactionDTO;
import com.myfinance.app.model.Transaction;
import com.myfinance.app.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testCreateTransaction() {
        // Cria e salva uma transacao
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Teste de transação");
        dto.setAmount(100.0);
        dto.setDate(LocalDateTime.now());
        dto.setCategory("Teste");
        dto.setIsIncome(true);

        Transaction saved = transactionService.createTransaction(dto);

        assertNotNull(saved.getId());
        assertEquals(dto.getDescription(), saved.getDescription());
    }

    @Test
    void testGetAllTransactions() {
        // Salva uma transacao e busca todas
        Transaction transaction = new Transaction();
        transaction.setDescription("Exemplo");
        transaction.setAmount(50.0);
        transaction.setDate(LocalDateTime.now());
        transaction.setCategory("Categoria");
        transaction.setIsIncome(true);
        transactionRepository.save(transaction);

        List<Transaction> transactions = transactionService.getAllTransactions();
        assertFalse(transactions.isEmpty());
    }

    @Test
    void testDeleteTransaction() {
        // Salva e deleta uma transacao
        Transaction transaction = new Transaction();
        transaction.setDescription("Exemplo Delete");
        transaction.setAmount(30.0);
        transaction.setDate(LocalDateTime.now());
        transaction.setCategory("Categoria Delete");
        transaction.setIsIncome(false);
        Transaction saved = transactionRepository.save(transaction);

        transactionService.deleteTransaction(saved.getId());

        assertFalse(transactionRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void testExportTransactionsToCsv() throws IOException {
        // Salva transacao e exporta para csv
        Transaction transaction = new Transaction();
        transaction.setDescription("Exemplo CSV");
        transaction.setAmount(200.0);
        transaction.setDate(LocalDateTime.now());
        transaction.setCategory("Categoria CSV");
        transaction.setIsIncome(true);
        transactionRepository.save(transaction);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transactionService.exportTransactionsToCsv(outputStream);

        String csvContent = outputStream.toString();
        assertTrue(csvContent.contains("Exemplo CSV"));
        assertTrue(csvContent.contains("Categoria CSV"));
    }
}