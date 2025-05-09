package com.myfinance.app.service;

import com.myfinance.app.dto.CashRegisterDTO;
import com.myfinance.app.model.CashRegister;
import com.myfinance.app.repository.CashRegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CashRegisterServiceTest {

    @Autowired
    private CashRegisterService service;

    @Autowired
    private CashRegisterRepository repository;

    @BeforeEach
    public void limparCaixasAbertos() {
        repository.findByIsClosedFalse().ifPresent(caixa -> {
            caixa.setCloseDate(LocalDateTime.now());
            repository.save(caixa);
        });
    }


    

    // Testa abrir caixa
    @Test
    public void testOpenCashRegister() {
        CashRegisterDTO dto = new CashRegisterDTO();
        dto.setInitialBalance(300.0);

        CashRegister caixa = service.openCashRegister(dto);

        assertNotNull(caixa.getId());
        assertNull(caixa.getCloseDate());
    }


    

    // Testa fechar caixa
    @Test
    public void testCloseCashRegister() {
        CashRegisterDTO dto = new CashRegisterDTO();
        dto.setInitialBalance(300.0);
        service.openCashRegister(dto);

        String resumo = service.closeCashRegister(600.0);

        assertTrue(resumo.contains("CAIXA FECHADO"));
        assertTrue(resumo.contains("Saldo Inicial: R$ 300,00"));
    }
}