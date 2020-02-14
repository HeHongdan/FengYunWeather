package per.dqq.design.pattern.structural.adapter;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        DC5V dc5V = new PowerAdapter(new AC220V());
        System.out.println("输出直流电：" + dc5V.outputDC5v() + "V");
    }
}
