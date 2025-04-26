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

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    // Cria e salva uma nova transacao
    public Transaction createTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setCategory(dto.getCategory());
        transaction.setIsIncome(dto.getIsIncome());

        return transactionRepository.save(transaction);
    }

    // Retorna todas as transacoes
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Retorna uma transacao pelo id
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id " + id));
    }

    // Deleta uma transacao pelo id
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    // Exporta as transacoes para csv
    public void exportTransactionsToCsv(OutputStream os) {
        List<Transaction> transactions = transactionRepository.findAll();

        try (Writer writer = new OutputStreamWriter(os);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Description", "Amount", "Date", "Category", "IsIncome"))) {

            double totalIncome = 0;
            double totalExpense = 0;

            // Escreve os dados das transacoes
            for (Transaction transaction : transactions) {
                csvPrinter.printRecord(
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getDate(),
                        transaction.getCategory(),
                        transaction.getIsIncome()
                );

                // Atualiza os totais de receita e despesa
                if (transaction.getIsIncome()) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpense -= transaction.getAmount();
                }
            }

            csvPrinter.println();

            // Adiciona o resumo
            csvPrinter.printRecord("Total Receita", totalIncome);
            csvPrinter.printRecord("Total Despesa", totalExpense);
            csvPrinter.printRecord("Saldo Final", totalIncome + totalExpense);

            csvPrinter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao exportar CSV", e);
        }
    }
}