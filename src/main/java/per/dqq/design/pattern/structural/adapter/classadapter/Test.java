package per.dqq.design.pattern.structural.adapter.classadapter;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        /**
         * 目标类调用自己的方法，通过Adapter被适配到Adaptee中对应的方法
         */
        Target adapter = new Adapter();
        adapter.target();
    }
}
