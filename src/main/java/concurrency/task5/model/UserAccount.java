package concurrency.task5.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserAccount implements Serializable {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final int userId;
    private final String name;
    private Set<Account> accounts;


    public UserAccount(String name) {
        this.name = name;
        this.accounts = ConcurrentHashMap.newKeySet();
        this.userId = idGenerator.getAndIncrement();
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return userId == that.userId && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name);
    }
}
