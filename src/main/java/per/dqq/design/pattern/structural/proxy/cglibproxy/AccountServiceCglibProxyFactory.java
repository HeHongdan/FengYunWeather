package per.dqq.design.pattern.structural.proxy.cglibproxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 *
 */
public class AccountServiceCglibProxyFactory implements MethodInterceptor {
    private AccountService target;

    public AccountServiceCglibProxyFactory(AccountService target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if ("transfer".equals(method.getName())) {
            System.out.println("start: " + System.currentTimeMillis());
            Object invoke = method.invoke(target, args);
            System.out.println("start: " + System.currentTimeMillis());
            return invoke;
        }
        return method.invoke(target, args);
    }

    /**
     * 定义代理的创建方法，函数名任意
     */
    AccountService createProxy() {
        // 创建增强器
        Enhancer enhancer = new Enhancer();
        // 设置目标类的父类
        enhancer.setSuperclass(AccountService.class);
        // 设置回调函数，MethodInterceptor extends Callback
        enhancer.setCallback(this);
        return (AccountService) enhancer.create();
    }
}
