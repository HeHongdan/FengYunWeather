package per.dqq.design.pattern.behavioral.strategy.v2;

import per.dqq.design.pattern.behavioral.strategy.PromotionStrategy;

/**
 *
 */
public class EmptyPromotion implements PromotionStrategy {
    @Override
    public void doPromotion() {
        System.out.println("空营销策略");
    }
}
