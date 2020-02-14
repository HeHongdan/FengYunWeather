package per.dqq.design.pattern.structural.proxy.jdkproxy;

/**
 *
 */
public class AccountServiceImpl implements IAccountService {
    @Override
    public void transfer() {
        System.out.println("主业务逻辑：转账");
    }

    @Override
    public void foo() {
        System.out.println("foo");
    }

    @Override
    public void bar() {
        System.out.println("bar");
    }
}
