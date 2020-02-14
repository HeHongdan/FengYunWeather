package per.dqq.design.pattern.structural.bridge;

/**
 *
 */
public abstract class Bank {
    protected Account account;

    public Bank(Account account) {
        this.account = account;
    }

    /**
     * Bank的openAccount()方法直接委托给内部持有的Account对象
     * 使用相同的方法名能更好的的体现委托关系
     */
    abstract Account openAccount();
}
