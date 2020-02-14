package per.dqq.design.pattern.creational.singleton;

/**
 * 静态内部类 & 线程安全 & 懒汉式
 *
 *
 */
public class StaticInnerClassSingleton {
    private StaticInnerClassSingleton() {

    }

    /**
     * JVM能够保证多线程对同一个类对象初始化时线程安全性
     */
    private static class InnerClass {
        private static StaticInnerClassSingleton instance = new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return InnerClass.instance;
    }
}
