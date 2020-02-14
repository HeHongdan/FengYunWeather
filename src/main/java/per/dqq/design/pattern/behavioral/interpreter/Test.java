package per.dqq.design.pattern.behavioral.interpreter;


/**
 */
public class Test {
    public static void main(String[] args) {
        String str = "6 100 11 + *";
        int res = new CustomExpressParser().parse(str);
        System.out.println(res);
    }
}
