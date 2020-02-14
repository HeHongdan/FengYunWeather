package per.dqq.design.pattern.structural.decorator.v2;

/**
 *
 */
public class BatterCake extends AbstractBatterCake {
    @Override
    protected String getDesc() {
        return "煎饼";
    }

    @Override
    protected int cost() {
        return 8;
    }
}
