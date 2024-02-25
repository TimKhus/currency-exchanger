package com.example.currencyexchanger.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "exchange_rates")
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @NotBlank
    @Column(name = "source_currency")
    Long sourceCurrencyId;

    @NotBlank
    @Column(name = "target_currency")
    Long targetCurrencyId;

    @NotBlank
    @Column(name = "rate", precision = 12, scale = 7)
    BigDecimal rate;
}
