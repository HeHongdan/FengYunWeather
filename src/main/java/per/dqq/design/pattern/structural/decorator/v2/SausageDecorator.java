package per.dqq.design.pattern.structural.decorator.v2;

/**
 *
 */
public class SausageDecorator extends AbstractDecorator {
    public SausageDecorator(AbstractBatterCake batterCake) {
        super(batterCake);
    }

    @Override
    protected String getDesc() {
        return super.getDesc() + " 加一个香肠";
    }

    @Override
    protected int cost() {
        return super.cost() + 2;
    }
}
