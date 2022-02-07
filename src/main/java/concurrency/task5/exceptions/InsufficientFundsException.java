package concurrency.task5.exceptions;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super();
    }

    public InsufficientFundsException(String msg) {
        super(msg);
    }
}
