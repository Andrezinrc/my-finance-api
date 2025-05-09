package com.myfinance.app.repository;

import com.myfinance.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.cashRegister.id = :cashRegisterId ORDER BY t.date")
    List<Transaction> findByCashRegisterIdOrderByDate(Long cashRegisterId);
}