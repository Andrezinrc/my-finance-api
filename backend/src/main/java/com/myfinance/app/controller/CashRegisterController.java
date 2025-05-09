package com.myfinance.app.controller;

import com.myfinance.app.dto.CashRegisterDTO;
import com.myfinance.app.model.CashRegister;
import com.myfinance.app.service.CashRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cash-register")
public class CashRegisterController {

    @Autowired
    private CashRegisterService cashRegisterService;

    @GetMapping("/is-open")
    public boolean isCashRegisterOpen() {
        return cashRegisterService.isCashRegisterOpen();
    }
    
    // ABRIR CAIXA
    @PostMapping("/open")
    public CashRegister openCashRegister(@RequestBody CashRegisterDTO dto) {
        return cashRegisterService.openCashRegister(dto);
    }

    // FECHAR CAIXA
    @PostMapping("/close")
    public String closeCashRegister(@RequestParam double physicalBalance) {
        return cashRegisterService.closeCashRegister(physicalBalance);
    }
}