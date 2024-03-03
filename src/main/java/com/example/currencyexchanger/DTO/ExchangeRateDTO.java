package com.example.currencyexchanger.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateDTO {
    private Long sourceCurrencyId;
    private Long targetCurrencyId;
    private BigDecimal rate;

}
