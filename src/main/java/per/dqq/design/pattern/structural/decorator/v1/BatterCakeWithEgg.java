package per.dqq.design.pattern.structural.decorator.v1;

/**
 *
 */
public class BatterCakeWithEgg extends BatterCake{
    @Override
    protected String getDesc() {
        return super.getDesc() + " 加一个鸡蛋";
    }

    @Override
    protected int cost() {
        return super.cost() + 1;
    }
}
