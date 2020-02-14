package per.dqq.design.pattern.structural.proxy.staticproxy;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        AccountServiceImplProxy proxy = new AccountServiceImplProxy(new AccountServiceImpl());
        proxy.transfer();
    }
}
