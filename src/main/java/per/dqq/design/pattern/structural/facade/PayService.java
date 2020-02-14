package per.dqq.design.pattern.structural.facade;

/**
 *
 */
public class PayService {
    /**
     * 礼物支付模块
     */
    public void pay(Gift gift) {
        System.out.println("支付 " + gift.getName() + "成功");
    }
}
