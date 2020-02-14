package per.dqq.design.pattern.structural.adapter.classadapter;

/**
 * 类适配器：通过继承被代理对象，然后使用super调用被代理对象的方法
 *
 *
 */
public class Adapter extends Adaptee implements Target {
    @Override
    public void target() {
        // 可以增加其它业务逻辑
        super.adapteeMethod();
        // 可以增加其它业务逻辑
    }
}
