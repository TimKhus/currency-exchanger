package com.example.currencyexchanger.controllers;

import com.example.currencyexchanger.models.Currency;
import com.example.currencyexchanger.services.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    CurrencyController (CurrencyService currencyService){
        this.currencyService = currencyService;
    }

    @GetMapping(path = "/currencies")
    public ResponseEntity<List<Currency>> getAll() {
        List<Currency> currencies = currencyService.getAllCurrencies();
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

}
