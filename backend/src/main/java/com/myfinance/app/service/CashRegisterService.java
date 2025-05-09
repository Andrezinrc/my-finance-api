package com.myfinance.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.myfinance.app.repository.CashRegisterRepository;
import com.myfinance.app.repository.TransactionRepository;
import com.myfinance.app.dto.CashRegisterDTO;
import com.myfinance.app.model.CashRegister;
import com.myfinance.app.model.Transaction;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CashRegisterService {

    @Autowired
    private CashRegisterRepository cashRegisterRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    // Buscar caixa
    public boolean isCashRegisterOpen() {
        return cashRegisterRepository.findByIsClosedFalse().isPresent();
    }



    
    // ABRIR CAIXA
    public CashRegister openCashRegister(CashRegisterDTO dto) {
        if (cashRegisterRepository.existsByIsClosedFalse()) {
            throw new IllegalStateException("Ja existe um caixa aberto!");
        }

        CashRegister cashRegister = new CashRegister();
        cashRegister.setInitialBalance(dto.getInitialBalance());
        cashRegister.setOpenDate(LocalDateTime.now());
        cashRegister.setClosed(false);
        return cashRegisterRepository.save(cashRegister);
    }

    


    // FECHAR CAIXA
    public String closeCashRegister(double physicalBalance) {
        CashRegister cashRegister = cashRegisterRepository.findByIsClosedFalse()
                .orElseThrow(() -> new IllegalStateException("Nenhum caixa aberto!"));

        List<Transaction> transactions = transactionRepository.findByCashRegisterIdOrderByDate(cashRegister.getId());

        double totalIncome = transactions.stream()
                .filter(Transaction::getIsIncome)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = transactions.stream()
                .filter(t -> !t.getIsIncome())
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expectedBalance = cashRegister.getInitialBalance() + totalIncome - totalExpenses;
        double difference = physicalBalance - expectedBalance;

        // Atualiza caixa
        cashRegister.setFinalBalance(physicalBalance);
        cashRegister.setCloseDate(LocalDateTime.now());
        cashRegister.setClosed(true);
        cashRegisterRepository.save(cashRegister);

        // Retorna relatorio resumido
        return String.format(
            "CAIXA FECHADO\n" +
            "Data: %s\n" +
            "Saldo Inicial: R$ %.2f\n" +
            "Total Vendas: R$ %.2f\n" +
            "Total Despesas: R$ %.2f\n" +
            "Saldo Esperado: R$ %.2f\n" +
            "Saldo Fisico: R$ %.2f\n" +
            "Diferenca: R$ %.2f",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            cashRegister.getInitialBalance(),
            totalIncome,
            totalExpenses,
            expectedBalance,
            physicalBalance,
            difference
        );
    }
}