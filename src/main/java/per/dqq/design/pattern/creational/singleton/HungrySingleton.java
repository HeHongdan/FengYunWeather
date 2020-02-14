package per.dqq.design.pattern.creational.singleton;

/**
 * 饿汉式单例模式
 *
 *
 */
public class HungrySingleton{
//    private static final HungrySingleton instance = new HungrySingleton();

    private static final HungrySingleton instance;

    static {
        /**
         * 适合初始化阶段需要较多加载配置步骤的场景，例如从配置文件中读取相应配置
         */
        instance = new HungrySingleton();
    }

    private HungrySingleton() {

    }

    public static HungrySingleton getInstance() {
        return instance;
    }
}
