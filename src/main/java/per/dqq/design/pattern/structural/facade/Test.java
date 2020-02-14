package per.dqq.design.pattern.structural.facade;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        GiftExchangeFacade fef = new GiftExchangeFacade();
        Gift gift = new Gift("gift");
        fef.giftExchange(gift);
    }
}
