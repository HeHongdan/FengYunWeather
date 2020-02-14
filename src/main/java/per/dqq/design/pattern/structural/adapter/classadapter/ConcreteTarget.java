package per.dqq.design.pattern.structural.adapter.classadapter;

/**
 *
 */
public class ConcreteTarget implements Target {
    @Override
    public void target() {
        System.out.println("具体Target实现类");
    }
}
