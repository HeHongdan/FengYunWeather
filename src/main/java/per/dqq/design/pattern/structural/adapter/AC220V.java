package per.dqq.design.pattern.structural.adapter;

/**
 *
 */
public class AC220V {
    public int inputAC220V() {
        int input = 220;
        System.out.println("输入交流电：" + input + "V");
        return input;
    }
}
