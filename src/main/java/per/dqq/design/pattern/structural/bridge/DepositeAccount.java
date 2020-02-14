package per.dqq.design.pattern.structural.bridge;

/**
 *
 */
public class DepositeAccount implements Account {
    @Override
    public Account openAccount() {
        System.out.println("打开一个定期账户");
        return new DepositeAccount();
    }

    @Override
    public void showAccountType() {
        System.out.println("定期账户");
    }
}
