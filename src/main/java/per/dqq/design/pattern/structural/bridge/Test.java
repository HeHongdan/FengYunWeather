package per.dqq.design.pattern.structural.bridge;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        ABCBank abcBank = new ABCBank(new DepositeAccount());
        Account account1 = abcBank.openAccount();
        account1.showAccountType();

        ICBCBank icbcBank = new ICBCBank(new SavingAccount());
        Account account2 = icbcBank.openAccount();
        account2.showAccountType();
    }
}
