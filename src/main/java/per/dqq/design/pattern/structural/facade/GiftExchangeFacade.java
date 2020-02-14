package per.dqq.design.pattern.structural.facade;

/**
 *
 */
public class GiftExchangeFacade {
    /**
     * 这里直接new出来，可以使用Spring的依赖注入
     */
    private CheckAuthorityService checkAuthorityService = new CheckAuthorityService();
    private PayService payService = new PayService();
    private ShippingService shippingService = new ShippingService();

    public void giftExchange(Gift gift) {
        if (checkAuthorityService.checkAuthority(gift)) {
            payService.pay(gift);
            String shippingNum = shippingService.shippingGift(gift);
            System.out.println(gift.getName() + "运输单号为：" + shippingNum);
        }
    }
}
