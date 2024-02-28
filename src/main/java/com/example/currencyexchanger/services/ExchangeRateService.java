package com.example.currencyexchanger.services;

import com.example.currencyexchanger.models.ExchangeRate;
import com.example.currencyexchanger.repositories.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public Optional<ExchangeRate> getExchangeRateById(Long id) {
        return exchangeRateRepository.findById(id);
    }

    public Optional<ExchangeRate> getExchangeRateByCurrenciesIds(Long id1, Long id2) {
        return exchangeRateRepository.findBySourceCurrencyIdAndTargetCurrencyId(id1, id2);
    }

    public ExchangeRate saveExchangeRate(ExchangeRate exchangeRate) {
        Optional<ExchangeRate> existingRate = exchangeRateRepository.findBySourceCurrencyIdAndTargetCurrencyId(
                exchangeRate.getSourceCurrencyId(), exchangeRate.getTargetCurrencyId()
        );

        if (existingRate.isPresent()) {
            ExchangeRate existingExchangeRate = existingRate.get();
            existingExchangeRate.setRate(exchangeRate.getRate());
            return exchangeRateRepository.save(existingExchangeRate);
        } else {
            return exchangeRateRepository.save(exchangeRate);
        }
    }

    public void deleteExchangeRate(Long id) {
        exchangeRateRepository.deleteById(id);
    }
}
