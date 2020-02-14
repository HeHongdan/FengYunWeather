package per.dqq.design.pattern.behavioral.strategy.v2;

import per.dqq.design.pattern.behavioral.strategy.FanXianPromotion;
import per.dqq.design.pattern.behavioral.strategy.LiJianPromotion;
import per.dqq.design.pattern.behavioral.strategy.ManJianPromotion;
import per.dqq.design.pattern.behavioral.strategy.PromotionStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略设计模式结合工厂设计模式，根据不同参数，让策略工厂产生不同策略
 *
 *
 */
public class PromotionStrategyFactory {
    private static Map<String, PromotionStrategy> PROMOTION_STRATEGY_MAP = new HashMap<>();
    private static PromotionStrategy NON_PROMOTION = new EmptyPromotion();

    static {
        PROMOTION_STRATEGY_MAP.put(PromotionKey.LIJIAN, new LiJianPromotion());
        PROMOTION_STRATEGY_MAP.put(PromotionKey.MANJIAN, new ManJianPromotion());
        PROMOTION_STRATEGY_MAP.put(PromotionKey.FANXIAN, new FanXianPromotion());
    }

    private interface PromotionKey {
        String LIJIAN = "LIJIAN";
        String MANJIAN = "MANJIAN";
        String FANXIAN = "FANXIAN";
    }

    public static PromotionStrategy getPromotionStrategy(String promotionKey) {
        PromotionStrategy promotionStrategy = PROMOTION_STRATEGY_MAP.get(promotionKey);
        if (promotionStrategy == null) {
            return NON_PROMOTION;
        } else {
            return promotionStrategy;
        }
    }
}
