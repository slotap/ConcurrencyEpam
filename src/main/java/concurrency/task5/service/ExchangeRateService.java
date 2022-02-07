package concurrency.task5.service;

import concurrency.task5.model.Currency;

import java.math.BigDecimal;
import java.util.*;

public class ExchangeRateService {
    private final Map<CurrencyPair, BigDecimal> conversionRates = new HashMap<>();
    private final Set<Currency> supportedCurrencies = new HashSet<>();

    private ExchangeRateService setConversionRate(Currency from, Currency to, BigDecimal rate) {
        conversionRates.put(new ExchangeRateService.CurrencyPair(from, to), rate);
        supportedCurrencies.add(from);
        supportedCurrencies.add(to);
        return this;
    }

    public ExchangeRateService setConversionRate(String from, String to, double rate) {
        return setConversionRate(Currency.getInstance(from), Currency.getInstance(to), BigDecimal.valueOf(rate));
    }

    public Set<Currency> getSupportedCurrencies() {
        return Collections.unmodifiableSet(supportedCurrencies);
    }

    public BigDecimal getConversionRate(Currency from, Currency to) {
        return conversionRates.get(new ExchangeRateService.CurrencyPair(from, to));
    }

    private static class CurrencyPair {
        private final Currency from;
        private final Currency to;

        CurrencyPair(Currency from, Currency to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExchangeRateService.CurrencyPair that = (ExchangeRateService.CurrencyPair) o;
            return Objects.equals(from, that.from) && Objects.equals(to, that.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }
}
