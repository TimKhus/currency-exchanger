package com.example.currencyexchanger.repositories;

import com.example.currencyexchanger.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findBySourceCurrencyIdAndTargetCurrencyId(Long id1, Long id2);
}
