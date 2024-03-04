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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class CurrencyExchangeController {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    @Autowired
    CurrencyExchangeController(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping(path = "/currencies")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        List<Currency> currencies = currencyService.getAllCurrencies();
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @GetMapping(path = "/currency/id/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable Long id) {
        Currency currency = currencyService.getCurrencyById(id).get();
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @GetMapping(path = "/currency/code/{code}")
    public ResponseEntity<Currency> getCurrencyByCode(@PathVariable String code) {
        Currency currency = currencyService.getCurrencyByCode(code.toUpperCase()).get();
        return new ResponseEntity<>(currency, HttpStatus.OK);
    }

    @DeleteMapping(path = "/currency/delete/{id}")
    public ResponseEntity<String> deleteCurrency(@PathVariable Long id) {
        String currencyName = currencyService.getCurrencyById(id).get().getFullName();
        currencyService.deleteCurrencyById(id);
        return new ResponseEntity<>("Currency " + currencyName + " deleted successfully", HttpStatus.OK);
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

    @DeleteMapping(path = "/exchange-rate/delete/{id}")
    public ResponseEntity<String> deleteExchangeRate(@PathVariable Long id) {
        exchangeRateService.deleteExchangeRate(id);
        return new ResponseEntity<>("Exchange rate " + id + " deleted successfully", HttpStatus.OK);
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

    @GetMapping(path = "/exchange")
    public ResponseEntity<String> performExchange(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount
    ) {
        Currency fromCurrency = currencyService.getCurrencyByCode(from.toUpperCase()).orElseThrow();
        Currency toCurrency = currencyService.getCurrencyByCode(to.toUpperCase()).orElseThrow();

        Optional<ExchangeRate> directRate = exchangeRateService.getExchangeRateByCurrenciesIds(
                fromCurrency.getId(), toCurrency.getId());

        if (!directRate.isPresent()) {
            Optional<ExchangeRate> reverseRate = exchangeRateService.getExchangeRateByCurrenciesIds(
                    toCurrency.getId(), fromCurrency.getId());
            if (reverseRate.isPresent()) {
                double convertedAmount = amount / reverseRate.get().getRate().doubleValue();
                return createResponse(amount, fromCurrency.getCode(), toCurrency.getCode(), reverseRate.get().getRate(), convertedAmount);
            }
        } else {
            double convertedAmount = amount * directRate.get().getRate().doubleValue();
            return createResponse(amount, fromCurrency.getCode(), toCurrency.getCode(), directRate.get().getRate(), convertedAmount);
        }

        Optional<ExchangeRate> usdFromRate = exchangeRateService.getExchangeRateByCurrenciesIds(1L, fromCurrency.getId());
        Optional<ExchangeRate> usdToRate = exchangeRateService.getExchangeRateByCurrenciesIds(1L, toCurrency.getId());

        if (usdFromRate.isPresent() && usdToRate.isPresent()) {
            double usdToFromCurrency = usdFromRate.get().getRate().doubleValue();
            double usdToToCurrency = usdToRate.get().getRate().doubleValue();
            double convertedAmount = (1 / usdToFromCurrency) * usdToToCurrency * amount;
            return createResponse(amount, fromCurrency.getCode(), toCurrency.getCode(), usdFromRate.get().getRate(), convertedAmount);
        }

        return new ResponseEntity<>("Exchange rate not available for the given currencies", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> createResponse(double amount, String fromCurrencyCode, String toCurrencyCode,
                                                  BigDecimal exchangeRate, double convertedAmount) {
        String response =String.format("Exchanging %.2f %s to %s.<br>Rate: %f<br>Converted amount: %.2f %s",
                amount, fromCurrencyCode, toCurrencyCode, exchangeRate, convertedAmount, toCurrencyCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

