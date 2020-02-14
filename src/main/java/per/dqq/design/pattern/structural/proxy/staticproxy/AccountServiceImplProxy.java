package per.dqq.design.pattern.structural.proxy.staticproxy;

/**
 *
 */
public class AccountServiceImplProxy implements IAccountService {
    private IAccountService iAccountService;

    public AccountServiceImplProxy(IAccountService iAccountService) {
        this.iAccountService = iAccountService;
    }

    @Override
    public void transfer() {
        // 在这里进行相应增强方法...
        System.out.println("【before】对目标类的transfer()方法进行增强");
        iAccountService.transfer();
        System.out.println("【after】对目标类的transfer()方法进行增强");
    }
}
