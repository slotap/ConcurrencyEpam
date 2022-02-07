package concurrency.task5.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;


public class Account implements Serializable {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final int id;
    private BigDecimal balance;
    private final Currency currency;
    private final LongAdder failCounter = new LongAdder();

    public Account(Currency currency) {
        Objects.requireNonNull(currency);
        this.id = idGenerator.getAndIncrement();
        this.balance = BigDecimal.ZERO;
        this.currency = currency;
    }

    public void deposit(final BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void withdraw(final BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public Currency getCurrency() {
        return currency;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }


    public void incFailedTransferCount() {
        failCounter.increment();
    }

    public long getFailCount() {
        return failCounter.sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(currency, account.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency);
    }
}
