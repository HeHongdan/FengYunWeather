package per.dqq.design.pattern.structural.bridge;

/**
 *
 */
public class ICBCBank extends Bank {
    public ICBCBank(Account account) {
        super(account);
    }

    @Override
    Account openAccount() {
        account.openAccount();
        return account;
    }
}
