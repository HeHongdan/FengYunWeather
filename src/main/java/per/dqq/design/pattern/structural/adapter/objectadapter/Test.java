package per.dqq.design.pattern.structural.adapter.objectadapter;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        Adaptee adaptee = new Adaptee();
        Target adapter = new  Adapter(adaptee);
        adapter.target();
    }
}
