package per.dqq.design.pattern.structural.proxy.cglibproxy;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        new AccountServiceCglibProxyFactory(new AccountService()).createProxy().transfer();
    }
}
