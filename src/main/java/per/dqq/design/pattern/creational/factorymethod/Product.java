package per.dqq.design.pattern.creational.factorymethod;

/**
 * 定义抽象类产品
 *
 *
 */
public abstract class Product {
    public void method01() {
        System.out.println("定义公共方法");
    }

    /**
     * 抽象方法，由子类具体产品实现
     */
    public abstract void method02();
}
