package per.dqq.design.pattern.structural.adapter.objectadapter;

/**
 * 对象适配器：通过Adapter内部持有被适配对象Adapee的引用（也可以通过构造函数引入）
 *
 *
 */
public class Adapter implements Target {
    private Adaptee adaptee;

    public Adapter(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void target() {
        adaptee.adapteeMethod();
    }
}
