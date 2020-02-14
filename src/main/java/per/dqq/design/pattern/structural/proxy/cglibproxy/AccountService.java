package per.dqq.design.pattern.structural.proxy.cglibproxy;

/**
 * 目标类，注意不是接口
 *
 *
 */
public class AccountService {
    public void transfer(){
        System.out.println("主业务逻辑：转账");
    }
}
