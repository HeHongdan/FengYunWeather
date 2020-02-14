package per.dqq.design.pattern.creational.singleton;

/**
 * Double check lock懒汉式单利模式
 *
 *
 */
public class DoubleCheckSingleton {
    private volatile static DoubleCheckSingleton instance = null;

    private DoubleCheckSingleton() {

    }

    private static DoubleCheckSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckSingleton.class) {
                if (instance == null) {
                    /**
                     * 注意这里使用volatile保证2、3步骤不会发生指令重排序
                     * 1. 分配原始内存空间
                     * 2. 在内存空间上初始化DoubleCheckSingleton对象
                     * 3. 将原始内存空间的首地址返回给instance
                     */
                    instance = new DoubleCheckSingleton();
                }
            }
        }
        return instance;
    }
}
