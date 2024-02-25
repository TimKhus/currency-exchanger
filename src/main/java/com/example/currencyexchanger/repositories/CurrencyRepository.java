package com.example.currencyexchanger.repositories;

import com.example.currencyexchanger.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
