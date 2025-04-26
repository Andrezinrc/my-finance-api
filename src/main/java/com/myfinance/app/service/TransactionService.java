package com.myfinance.app.service;

import com.myfinance.app.model.Transaction;
import com.myfinance.app.dto.TransactionDTO;
import com.myfinance.app.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;

    // Cria e salva uma nova transacao
    public Transaction createTransaction(TransactionDTO dto){
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
}