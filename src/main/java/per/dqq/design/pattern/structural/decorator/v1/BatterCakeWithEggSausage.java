package per.dqq.design.pattern.structural.decorator.v1;

/**
 *
 */
public class BatterCakeWithEggSausage extends BatterCakeWithEgg {
    @Override
    protected String getDesc() {
        return super.getDesc() + " 加一个香肠";
    }

    @Override
    protected int cost() {
        return super.cost() + 2;
    }
}
