package per.dqq.design.pattern.creational.factorymethod;

/**
 * 客户端测试类
 *
 *
 */
public class Test {
    public static void main(String[] args) {
        ConcreteCreator01 creator01 = new ConcreteCreator01();
        ConcreteCreator02 creator02 = new ConcreteCreator02();
        creator01.createProduct().method02();
        creator02.createProduct().method02();
    }
}
