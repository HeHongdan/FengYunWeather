package per.dqq.design.pattern.structural.bridge;

/**
 *
 */
public class ABCBank extends Bank {
    public ABCBank(Account account) {
        super(account);
    }

    @Override
    Account openAccount() {
        account.openAccount();
        return account;
    }
}
