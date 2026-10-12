package com.cryptovault.service.impl;

import com.cryptovault.dto.MarketTickerDto;
import com.cryptovault.entity.Currency;
import com.cryptovault.repository.CurrencyRepository;
import com.cryptovault.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    private final CurrencyRepository currencyRepository;
    private final Random random = new Random();

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> getAllActiveCurrencies() {
        return currencyRepository.findAllByActiveTrueOrderByCodeAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Currency> getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code.toUpperCase());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Currency> getCurrencyById(Long id) {
        return currencyRepository.findById(id);
    }

    @Override
    @Transactional
    public Currency saveCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public Currency updateExchangeRates(Long currencyId, BigDecimal inrRate, BigDecimal usdRate, BigDecimal change24h) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new IllegalArgumentException("Currency not found with id: " + currencyId));

        if (inrRate != null && inrRate.compareTo(BigDecimal.ZERO) > 0) {
            currency.setExchangeRateInr(inrRate);
        }
        if (usdRate != null && usdRate.compareTo(BigDecimal.ZERO) > 0) {
            currency.setExchangeRateUsd(usdRate);
        }
        if (change24h != null) {
            currency.setChange24h(change24h);
        }
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarketTickerDto> getMarketTickers() {
        return getAllActiveCurrencies().stream().map(c -> MarketTickerDto.builder()
                .id(c.getId())
                .code(c.getCode())
                .name(c.getName())
                .symbol(c.getSymbol())
                .priceInr(c.getExchangeRateInr())
                .priceUsd(c.getExchangeRateUsd())
                .change24h(c.getChange24h() != null ? c.getChange24h() : BigDecimal.ZERO)
                .marketCapUsd(c.getMarketCapUsd() != null ? c.getMarketCapUsd() : BigDecimal.ZERO)
                .volume24hUsd(c.getVolume24hUsd() != null ? c.getVolume24hUsd() : BigDecimal.ZERO)
                .iconClass(c.getIconClass())
                .badgeColor(c.getBadgeColor())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    @Scheduled(fixedRate = 45000) // update market fluctuations every 45 seconds
    @Transactional
    public void simulateMarketFluctuations() {
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.isEmpty()) {
            return;
        }

        for (Currency c : currencies) {
            // Random fluctuation between -0.4% and +0.45%
            double deltaPercent = (random.nextDouble() * 0.9) - 0.42;
            BigDecimal factor = BigDecimal.valueOf(1 + (deltaPercent / 100.0));

            BigDecimal newInr = c.getExchangeRateInr().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            BigDecimal newUsd = c.getExchangeRateUsd().multiply(factor).setScale(4, RoundingMode.HALF_UP);

            BigDecimal currentChange = c.getChange24h() != null ? c.getChange24h() : BigDecimal.ZERO;
            BigDecimal newChange = currentChange.add(BigDecimal.valueOf(deltaPercent)).setScale(2, RoundingMode.HALF_UP);

            c.setExchangeRateInr(newInr);
            c.setExchangeRateUsd(newUsd);
            c.setChange24h(newChange);
            currencyRepository.save(c);
        }
    }
}
