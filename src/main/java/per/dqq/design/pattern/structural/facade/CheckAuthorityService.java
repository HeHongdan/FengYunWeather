package per.dqq.design.pattern.structural.facade;

/**
 *
 */
public class CheckAuthorityService {

    public boolean checkAuthority(Gift gift) {
        /**
         * 各种权限校验。。。
         */
        System.out.println("校验 " + gift.getName() + "成功");
        return true;
    }
}
