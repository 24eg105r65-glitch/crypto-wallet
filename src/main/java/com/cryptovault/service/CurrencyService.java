package com.cryptovault.service;

import com.cryptovault.dto.MarketTickerDto;
import com.cryptovault.entity.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<Currency> getAllActiveCurrencies();
    List<Currency> getAllCurrencies();
    Optional<Currency> getCurrencyByCode(String code);
    Optional<Currency> getCurrencyById(Long id);
    Currency saveCurrency(Currency currency);
    Currency updateExchangeRates(Long currencyId, BigDecimal inrRate, BigDecimal usdRate, BigDecimal change24h);
    List<MarketTickerDto> getMarketTickers();
    void simulateMarketFluctuations();
}
