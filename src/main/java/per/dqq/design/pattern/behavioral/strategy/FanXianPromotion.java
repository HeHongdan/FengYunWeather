package per.dqq.design.pattern.behavioral.strategy;

/**
 *
 */
public class FanXianPromotion implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("返现促销策略");
    }
}
