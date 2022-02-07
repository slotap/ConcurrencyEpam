package concurrency.task5.service;

import concurrency.task5.exceptions.AccountAlreadyExistsException;
import concurrency.task5.exceptions.CurrencyNotSupportedException;
import concurrency.task5.model.Account;
import concurrency.task5.model.Currency;
import concurrency.task5.model.UserAccount;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class AccountService {
    private final ExchangeRateService exchangeRateService;

    public AccountService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public void saveToFile(UserAccount account, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            File existingAccountFile = new File(fileName);
            synchronized (account) {
                if (existingAccountFile.exists()) {
                    existingAccountFile.delete();
                    oos.writeObject(account);
                } else {
                    oos.writeObject(account);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserAccount createUserAccount(String user1) {
        return new UserAccount(user1);
    }

    public Optional<Account> getCurrencyAccount(UserAccount user, String currencyName) {
        return user.getAccounts().stream()
                .filter(acc -> acc.getCurrency().getCurrencyName().equals(currencyName))
                .findFirst();
    }

    public void addAccount(UserAccount user1, String currency, ExchangeRateService supportedCurrencies, BigDecimal initialDeposit) throws AccountAlreadyExistsException, CurrencyNotSupportedException {
        Account newAccount;
        synchronized (user1) {
            if (validateCurrencyIsSupported(currency, supportedCurrencies)) {
                newAccount = new Account(new Currency(currency));
                newAccount.deposit(initialDeposit);
            } else {
                throw new CurrencyNotSupportedException();
            }

            if (validateAccountWithCurrencyExists(user1, newAccount)) {
                throw new AccountAlreadyExistsException();
            } else {
                user1.getAccounts().add(newAccount);
            }
        }
    }

    private boolean validateAccountWithCurrencyExists(UserAccount user1, Account newAccount) {
        return user1.getAccounts().stream()
                .map(account -> account.getCurrency().getCurrencyName())
                .anyMatch(accountCurr -> accountCurr.equals(newAccount.getCurrency().getCurrencyName()));
    }

    private boolean validateCurrencyIsSupported(String currency, ExchangeRateService supportedCurrencies) {
        return supportedCurrencies.getSupportedCurrencies().stream()
                .anyMatch(suppCurrencies -> suppCurrencies.getCurrencyName().equals(currency));
    }

    public BigDecimal exchangeCurrency(Account accFrom, Account accTo, BigDecimal amount) {
        return amount.divide(exchangeRateService.getConversionRate(accFrom.getCurrency(), accTo.getCurrency()), RoundingMode.DOWN);
    }
}
