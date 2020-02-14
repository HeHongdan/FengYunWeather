package per.dqq.design.pattern.structural.decorator.v2;

/**
 * 装饰者模式实现：一个煎饼加两个鸡蛋，一个香肠
 *
 *
 */
public class Test {
    public static void main(String[] args) {
        AbstractBatterCake batterCake = new BatterCake();
        batterCake = new EggDecorator(batterCake);
        batterCake = new EggDecorator(batterCake);
        batterCake = new SausageDecorator(batterCake);
        System.out.println(batterCake.getDesc() + "，花费：" + batterCake.cost());
    }
}
