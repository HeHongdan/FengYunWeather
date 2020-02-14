package per.dqq.design.pattern.behavioral.strategy;

/**
 *
 */
public class LiJianPromotion implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("立减促销策略");
    }
}
