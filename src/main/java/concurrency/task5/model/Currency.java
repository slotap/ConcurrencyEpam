package concurrency.task5.model;

import java.io.Serializable;
import java.util.Objects;

public class Currency implements Serializable {
    private final String currencyName;

    public Currency(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public static Currency getInstance(String name) {
        return new Currency(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(currencyName, currency.currencyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyName);
    }
}
