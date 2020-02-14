package per.dqq.design.pattern.creational.factorymethod;

/**
 * 具体工厂类
 *
 *
 */
public class ConcreteCreator01 extends Creator {

    @Override
    public Product createProduct() {
        return new ConcreteProduct01();
    }
}
