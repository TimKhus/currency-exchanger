package com.example.currencyexchanger.repositories;

import com.example.currencyexchanger.models.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
