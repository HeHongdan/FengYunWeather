package per.dqq.design.pattern.behavioral.strategy.v2;

import per.dqq.design.pattern.behavioral.strategy.PromotionStrategy;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        String[] strategies = {"MANJIAN", "LIJIAN", "FANXIAN"};
        for (String strategy : strategies) {
            PromotionStrategy promotionStrategy = PromotionStrategyFactory.getPromotionStrategy(strategy);
            promotionStrategy.doPromotion();
        }
    }
}
