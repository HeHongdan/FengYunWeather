package per.dqq.design.pattern.structural.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 */
public class AccountServiceJDKProxy implements InvocationHandler {
    private Object object;

    public AccountServiceJDKProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        if (method.getName().length() == 3) {
            System.out.println("【before】对目标类的transfer()方法进行增强");
            result = method.invoke(object, args);
            System.out.println("【after】对目标类的transfer()方法进行增强");
        }
        return result;
    }
}
