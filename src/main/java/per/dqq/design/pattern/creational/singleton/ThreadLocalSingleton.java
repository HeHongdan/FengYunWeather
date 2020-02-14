package per.dqq.design.pattern.creational.singleton;

/**
 * 仅保证同一个线程内部对象是单例的
 * 同步锁：时间换空间
 * ThreadLocal：空间换时间
 *
 *
 */
public class ThreadLocalSingleton {
    private static final ThreadLocal<ThreadLocalSingleton> threadLocalSingleton
            = new ThreadLocal<ThreadLocalSingleton>() {
        @Override
        protected ThreadLocalSingleton initialValue() {
            return new ThreadLocalSingleton();
        }
    };

    private ThreadLocalSingleton() {

    }

    public static ThreadLocalSingleton getInstance() {
        return threadLocalSingleton.get();
    }
}
