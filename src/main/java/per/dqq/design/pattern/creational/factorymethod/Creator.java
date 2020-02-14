package per.dqq.design.pattern.creational.factorymethod;

/**
 * 抽象类工厂，可以根据具体参数（String、Enum、Class等进行创建）
 *
 *
 */
public abstract class Creator {
    public abstract Product createProduct();
}
