package com.myfinance.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.myfinance.app.model.CashRegister;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister, Long> {
    
    boolean existsByIsClosedFalse();
    
    Optional<CashRegister> findByIsClosedFalse();

    List<CashRegister> findByOpenDateBetween(LocalDateTime start, LocalDateTime end);
    
    Optional<CashRegister> findFirstByIsClosedTrueOrderByCloseDateDesc();
    
    @Query("SELECT AVG(c.initialBalance) FROM CashRegister c WHERE c.openDate BETWEEN :start AND :end")
    Double findAverageInitialBalanceByPeriod(LocalDateTime start, LocalDateTime end);
}