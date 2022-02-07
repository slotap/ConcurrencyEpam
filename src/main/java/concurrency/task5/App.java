package concurrency.task5;

import concurrency.task5.exceptions.AccountAlreadyExistsException;
import concurrency.task5.exceptions.CurrencyAccountNotExistException;
import concurrency.task5.exceptions.CurrencyNotSupportedException;
import concurrency.task5.model.Account;
import concurrency.task5.model.UserAccount;
import concurrency.task5.service.AccountService;
import concurrency.task5.service.ExchangeRateService;
import concurrency.task5.utilities.Transfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Random rnd = new Random();

        // sample exchange rates
        ExchangeRateService exchangeRateService = new ExchangeRateService()
                .setConversionRate("USD", "PLN", 4.0)
                .setConversionRate("PLN", "USD", 0.25)
                .setConversionRate("PLN", "PLN", 1)
                .setConversionRate("USD", "USD", 1)
                .setConversionRate("EUR", "EUR", 1)
                .setConversionRate("EUR", "PLN", 5.0)
                .setConversionRate("PLN", "EUR", 0.2)
                .setConversionRate("USD", "EUR", 0.834)
                .setConversionRate("EUR", "USD", 1.2);

        AccountService accountService = new AccountService(exchangeRateService);

        // sample userAccount
        UserAccount user1 = accountService.createUserAccount("user1");

        ScheduledExecutorService amountMonitoring = createSuccessMonitoringThread(user1);

        // add different currency accounts to sample user
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.execute(() -> {
            try {
                accountService.addAccount(user1, "PLN", exchangeRateService, BigDecimal.valueOf(1000));
                accountService.addAccount(user1, "EUR", exchangeRateService, BigDecimal.valueOf(1000));
                accountService.addAccount(user1, "USD", exchangeRateService, BigDecimal.valueOf(1000));
                accountService.saveToFile(user1, "user1.txt");
            } catch (AccountAlreadyExistsException | CurrencyNotSupportedException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();


        // create list of transactions between user accounts
        List<Transfer> transfers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            try {
                transfers.add(new Transfer(user1, "PLN", "USD", BigDecimal.valueOf(rnd.nextInt(100)), accountService));
                transfers.add(new Transfer(user1, "PLN", "EUR", BigDecimal.valueOf(rnd.nextInt(100)), accountService));
                transfers.add(new Transfer(user1, "USD", "EUR", BigDecimal.valueOf(rnd.nextInt(50)), accountService));
                transfers.add(new Transfer(user1, "EUR", "PLN", BigDecimal.valueOf(rnd.nextInt(50)), accountService));
            } catch (CurrencyAccountNotExistException e) {
                e.printStackTrace();
            }
        }

        ExecutorService serviceTransfers = Executors.newFixedThreadPool(3);
        List<Future<Boolean>> result = serviceTransfers.invokeAll(transfers);
        serviceTransfers.shutdown();

        // display result of transactions using Future
        System.out.println("Future results:");

        for (int i = 0; i < result.size(); i++) {
            Future<Boolean> future = result.get(i);
            Transfer transfer = transfers.get(i);
            try {
                System.out.println("[" + transfer.getId() + "] Transfer: " + future.get());
            } catch (ExecutionException e) {
                System.out.println("[" + transfer.getId() + "] Transfer: " + e.getMessage());
            }
        }

        amountMonitoring.shutdown();
    }

    private static ScheduledExecutorService createSuccessMonitoringThread(final UserAccount acc1) {
        ScheduledExecutorService amountMonitoring = Executors
                .newScheduledThreadPool(1);
        amountMonitoring.scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("No. of failed transfers in User1 accounts: "
                        + getSum(acc1));
            }
        }, 2, 3, TimeUnit.SECONDS);
        return amountMonitoring;
    }

    private static long getSum(UserAccount acc1) {
        return acc1.getAccounts().stream()
                .map(Account::getFailCount)
                .mapToLong(Long::longValue)
                .sum();
    }
}
