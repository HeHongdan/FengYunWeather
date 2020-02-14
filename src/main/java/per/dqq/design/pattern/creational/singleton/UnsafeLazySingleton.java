package per.dqq.design.pattern.creational.singleton;

/**
 * 非线程安全懒汉式单例模式
 *
 *
 */
public class UnsafeLazySingleton {
    private static UnsafeLazySingleton instance = null;

    private UnsafeLazySingleton() {

    }

    public static UnsafeLazySingleton getInstance() {
        if (instance == null) {
            instance = new UnsafeLazySingleton();
        }
        return instance;
    }
}
