package com.example.currencyexchanger.controllers;

import com.example.currencyexchanger.DTO.CurrencyDTO;
import com.example.currencyexchanger.DTO.ExchangeRateDTO;
import com.example.currencyexchanger.models.Currency;
import com.example.currencyexchanger.models.ExchangeRate;
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
    public ResponseEntity<List<Currency>> getAllCurrencies() {
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
    public ResponseEntity<Currency> saveCurrency(@RequestBody CurrencyDTO currencyDTO) {
        Currency currencyToSave = new Currency();
        currencyToSave.setCode(currencyDTO.getCode());
        currencyToSave.setFullName(currencyDTO.getFullName());
        currencyToSave.setSign(currencyDTO.getSign());
        currencyService.saveCurrency(currencyToSave);
        return new ResponseEntity<>(currencyToSave, HttpStatus.CREATED);
    }

    @GetMapping(path = "/exchange-rates")
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllExchangeRates();
        return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
    }

    @GetMapping(path = "exchange-rate/id/{id1}-{id2}")
    public ResponseEntity<ExchangeRate> getExchangeRateByIds(@PathVariable Long id1, @PathVariable Long id2) {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCurrenciesIds(id1, id2).orElseThrow();
        return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
    }

    @PostMapping(path = "/exchange-rate")
    public ResponseEntity<ExchangeRate> saveExchangeRate(@RequestBody ExchangeRateDTO exchangeRateDTO) {
        ExchangeRate exchangeRateToSave = new ExchangeRate();
        exchangeRateToSave.setSourceCurrencyId(exchangeRateDTO.getSourceCurrencyId());
        exchangeRateToSave.setTargetCurrencyId(exchangeRateDTO.getTargetCurrencyId());
        exchangeRateToSave.setRate(exchangeRateDTO.getRate());
        exchangeRateService.saveExchangeRate(exchangeRateToSave);
        return new ResponseEntity<>(exchangeRateToSave, HttpStatus.OK);
    }

}

