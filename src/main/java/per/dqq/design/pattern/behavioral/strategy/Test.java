package per.dqq.design.pattern.behavioral.strategy;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        // 返现活动
        PromotionActivity activity1 = new PromotionActivity(new FanXianPromotion());
        activity1.executePromotion();

        // 满减活动
        PromotionActivity activity2 = new PromotionActivity(new ManJianPromotion());
        activity2.executePromotion();

        // 立减活动
        PromotionActivity activity3 = new PromotionActivity(new LiJianPromotion());
        activity3.executePromotion();
    }
}
