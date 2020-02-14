package per.dqq.design.pattern.creational.factorymethod;

/**
 *
 */
public class ConcreteCreator02 extends Creator {
    @Override
    public Product createProduct() {
        return new ConcreteProduct02();
    }
}
