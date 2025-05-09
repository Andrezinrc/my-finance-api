package com.myfinance.app.service;

import com.myfinance.app.dto.TransactionDTO;
import com.myfinance.app.model.Transaction;
import com.myfinance.app.repository.TransactionRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.*;

import com.myfinance.app.dto.CashRegisterDTO;


@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CashRegisterService cashRegisterService;

    @Test
    @Transactional
    void testCreateTransaction() {
        testExportTransactionsToCsv();

        // Abre um caixa
        CashRegisterDTO cashDto = new CashRegisterDTO();
        cashDto.setInitialBalance(1000.0);
        //cashRegisterService.openCashRegister(cashDto);
            
        // Cria e salva uma transacao
        TransactionDTO dto = new TransactionDTO();
        dto.setDescription("Teste de transacao");
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
    void testExportTransactionsToCsv() {
        // Cria e salva a transacao
        Transaction transaction = new Transaction();
        transaction.setDescription("Exemplo CSV");
        transaction.setAmount(200.0);
        transaction.setDate(LocalDateTime.of(2025, 4, 10, 0, 0));
        transaction.setCategory("Categoria CSV");
        transaction.setIsIncome(true);
        transactionRepository.save(transaction);

        List<Transaction> transactions = List.of(transaction);
        int year = 2025;
        int month = 4;

        transactionService.exportTransactionsToCsv(transactions, year, month);

        // Verifica se o arquivo foi criado
        File file = new File("relatorios/2025/04/relatorio.csv");
        assertTrue(file.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String content = reader.lines().reduce("", (a, b) -> a + b);
            assertTrue(content.contains("Exemplo CSV"));
            assertTrue(content.contains("Categoria CSV"));
        } catch (IOException e) {
            fail("Erro ao ler o arquivo CSV gerado");
        }
    }
}