package per.dqq.design.pattern.structural.decorator.v1;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        BatterCake batterCake = new BatterCake();
        System.out.println(batterCake.getDesc() + "，消费" + batterCake.cost());
        batterCake = new BatterCakeWithEgg();
        System.out.println(batterCake.getDesc() + "，消费" + batterCake.cost());
        batterCake = new BatterCakeWithEggSausage();
        System.out.println(batterCake.getDesc() + "，消费" + batterCake.cost());

        /**
         * 如果需要自定义加入n个鸡蛋、m个香肠，通过继承的方式会出现类爆炸问题
         */
    }
}
