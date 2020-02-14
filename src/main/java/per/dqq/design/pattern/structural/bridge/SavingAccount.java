package per.dqq.design.pattern.structural.bridge;

/**
 *
 */
public class SavingAccount implements Account {
    @Override
    public Account openAccount() {
        System.out.println("打开一个活期账户");
        return new SavingAccount();
    }

    @Override
    public void showAccountType() {
        System.out.println("活期账户");
    }
}
