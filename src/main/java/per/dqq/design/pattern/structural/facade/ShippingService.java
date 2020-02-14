package per.dqq.design.pattern.structural.facade;

/**
 *
 */
public class ShippingService {
    public String shippingGift(Gift gift) {
        System.out.println("订单编号 " + gift.getName() + "成功");
        return "2571";
    }
}
