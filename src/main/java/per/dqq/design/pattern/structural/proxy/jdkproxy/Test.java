package per.dqq.design.pattern.structural.proxy.jdkproxy;

import java.lang.reflect.Proxy;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        IAccountService target = new AccountServiceImpl();
        IAccountService service = (IAccountService) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AccountServiceJDKProxy(target));
        service.transfer();
        service.foo();
        System.out.println("=============================");
        service.bar();
    }
}
