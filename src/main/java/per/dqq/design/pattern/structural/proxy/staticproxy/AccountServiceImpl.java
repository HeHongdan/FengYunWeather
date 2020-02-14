package per.dqq.design.pattern.structural.proxy.staticproxy;

/**
 *
 */
public class AccountServiceImpl implements IAccountService {
    @Override
    public void transfer() {
        System.out.println("主业务逻辑：转账");
    }
}
