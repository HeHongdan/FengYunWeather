package per.dqq.design.pattern.structural.decorator.v2;

/**
 * 注意AbstractDecorator可以是abstract类，也可以不是，具体业务需求：是否有比如让子类实现的方法
 *
 *
 */
public class AbstractDecorator extends AbstractBatterCake {
    /**
     * 内部持有一个AbstractBatterCake对象，并对该对象进行增强
     */
    private AbstractBatterCake batterCake;

    public AbstractDecorator(AbstractBatterCake batterCake) {
        this.batterCake = batterCake;
    }

    @Override
    protected String getDesc() {
        return batterCake.getDesc();
    }

    @Override
    protected int cost() {
        return batterCake.cost();
    }
}
