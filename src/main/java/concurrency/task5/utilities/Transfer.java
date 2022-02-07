package concurrency.task5.utilities;

import concurrency.task5.exceptions.CurrencyAccountNotExistException;
import concurrency.task5.exceptions.InsufficientFundsException;
import concurrency.task5.model.Account;
import concurrency.task5.model.UserAccount;
import concurrency.task5.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class Transfer implements Callable<Boolean> {

    Logger logger = LoggerFactory.getLogger(Transfer.class);
    private final AccountService accountService;

    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private static final int MAX_TRANSFER_SEC = 7;

    private final Account accFrom;
    private final Account accTo;
    private final BigDecimal amount;

    private final int id;

    private final Random waitRandom = new Random();

    public Transfer(UserAccount user, String accFrom, String accTo, BigDecimal amount, AccountService accountService) throws CurrencyAccountNotExistException {
        this.id = idGenerator.getAndIncrement();
        this.accFrom = accountService.getCurrencyAccount(user, accFrom).orElseThrow(CurrencyAccountNotExistException::new);
        this.accTo = accountService.getCurrencyAccount(user, accTo).orElseThrow(CurrencyAccountNotExistException::new);
        this.amount = amount;
        this.accountService = accountService;
    }

    @Override
    public Boolean call() throws Exception {
        if (accFrom.getBalance().compareTo(amount) < 0) {
            accFrom.incFailedTransferCount();
            logger.error("Insufficient funds in Account " + accFrom.getId());
            throw new InsufficientFundsException("Insufficient funds in Account " + accFrom.getId());
        }
        accFrom.withdraw(amount);
        accTo.deposit(accountService.exchangeCurrency(accFrom, accTo, amount));
        //simulate different time for executing
        Thread.sleep(waitRandom.nextInt(MAX_TRANSFER_SEC * 1000));
        logger.info(("[" + id + "] " + "Transfer " + amount + " done from " + accFrom.getId() + " to " + accTo.getId()));
        return true;
    }

    public int getId() {
        return id;
    }

}