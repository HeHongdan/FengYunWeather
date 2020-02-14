package per.dqq.design.pattern.creational.singleton;

/**
 * 使用synchronized关键字实现线程安全的懒汉式单例模式
 *
 *
 */
public class SafeLazySingleton {
    private static SafeLazySingleton instance = null;

    private SafeLazySingleton() {

    }
//    synchronized 方法
//    public synchronized static SafeLazySingleton getInstance() {
//        if (instance == null) {
//            instance = new SafeLazySingleton();
//        }
//        return instance;
//    }

    // synchronized锁住代码块
    public static SafeLazySingleton getInstance() {
        synchronized (SafeLazySingleton.class) {
            if (instance == null) {
                instance = new SafeLazySingleton();
            }
        }
        return instance;
    }
}
