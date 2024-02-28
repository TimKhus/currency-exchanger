package com.example.currencyexchanger.controllers;

import com.example.currencyexchanger.models.Currency;
import com.example.currencyexchanger.services.CurrencyService;
import com.example.currencyexchanger.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CurrencyExchangeController {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    CurrencyExchangeController(CurrencyService currencyService, ExchangeRateService exchangeRateService){
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping(path = "/currencies")
    public ResponseEntity<List<Currency>> getAll() {
        List<Currency> currencies = currencyService.getAllCurrencies();
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @GetMapping(path="/currency/id/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable Long id) {
        Currency currency = currencyService.getCurrencyById(id).get();
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @GetMapping(path = "/currency/code/{code}")
    public ResponseEntity<Currency> getCurrencyByCode(@PathVariable String code) {
        Currency currency = currencyService.getCurrencyByCode(code.toUpperCase()).get();
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @PostMapping(path = "/currency")
    public ResponseEntity<Currency> saveCurrency(@RequestBody Currency currency) {
        Currency currencyToSave = currencyService.saveCurrency(currency);
        return new ResponseEntity<>(currencyToSave, HttpStatus.CREATED);
    }
}
