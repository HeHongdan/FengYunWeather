package per.dqq.design.pattern.behavioral.strategy;

/**
 *
 */
public class ManJianPromotion implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("满减促销策略");
    }
}
