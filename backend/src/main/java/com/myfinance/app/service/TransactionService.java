package com.myfinance.app.service;

import com.myfinance.app.model.Transaction;
import com.myfinance.app.dto.TransactionDTO;
import com.myfinance.app.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.File;
import java.io.FileWriter;
import com.myfinance.app.model.CashRegister;
import com.myfinance.app.repository.CashRegisterRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CashRegisterRepository cashRegisterRepository;

    // Cria e salva uma nova transacao
    public Transaction createTransaction(TransactionDTO dto) {
        closePreviousMonthIfNeeded();

        if (dto.getAmount() <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Descricao e obrigatoria");
        }

        CashRegister openCashRegister = cashRegisterRepository.findByIsClosedFalse()
            .orElseThrow(() -> new IllegalStateException("Nao e possivel registrar transacao: caixa nao esta aberto!"));
    
        Transaction transaction = new Transaction();
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now()); // Data do DTO ou atual
        transaction.setCategory(dto.getCategory());
        transaction.setIsIncome(dto.getIsIncome());
        openCashRegister.addTransaction(transaction);

        return transactionRepository.save(transaction);
    }

    // Retorna todas as transacoes
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Retorna uma transacao pelo id
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacao nao encontrada com ID " + id));
    }

    // Deleta uma transacao pelo id
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    // Exporta as transacoes para csv
    public void exportTransactionsToCsv(List<Transaction> transactions, int year, int month) {
        String folderPath = "relatorios/" + year + "/" + String.format("%02d", month);
        new File(folderPath).mkdirs();

        String filePath = folderPath + "/relatorio.csv";

        try (Writer writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Description", "Amount", "Date", "Category", "IsIncome"))) {

            double totalIncome = 0;
            double totalExpense = 0;

            for (Transaction transaction : transactions) {
                csvPrinter.printRecord(
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getCategory(),
                        transaction.getIsIncome()
                );

                if (transaction.getIsIncome()) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpense -= transaction.getAmount();
                }
            }

            csvPrinter.println();
            csvPrinter.printRecord("Total Receita", totalIncome);
            csvPrinter.printRecord("Total Despesa", totalExpense);
            csvPrinter.printRecord("Saldo Final", totalIncome + totalExpense);

            csvPrinter.flush();
    
        } catch (IOException e) {
            throw new RuntimeException("Erro ao exportar CSV", e);
        }
    }

    // Fecha o mes anterior, se necessario
    public void closePreviousMonthIfNeeded() {
        List<Transaction> transactions = transactionRepository.findAll();

        if (transactions.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        Transaction t = transactions.get(0);
        int lastMonth = t.getDate().getMonthValue();
        int lastYear = t.getDate().getYear();

        if (lastMonth != now.getMonthValue() || lastYear != now.getYear()) {
            exportTransactionsToCsv(transactions, lastYear, lastMonth);
            transactionRepository.deleteAll();
        }
    }
}