package per.dqq.design.pattern.creational.singleton;

import java.io.Serializable;

/**
 * 通过定义readResolve()方法序列化破坏单例模式
 *
 *
 */
public class SerializableSingleton implements Serializable {
    private static SerializableSingleton instance = new SerializableSingleton();

    /**
     * 增加单例模式防御性代码，但是只能针对恶汉式和静态内部类形式
     * 对于其他形式，防御性代码不一定生效。要看反射创建对象的线程和获取单例对象的线程两者的执行顺序：
     *      - 若获取单例对象的线程先执行，那么防御性代码有效
     *      - 若反射创建对象的线程先执行，那么防御性代码无效，会有多个对象产生
     */
    private SerializableSingleton() {
        if(instance != null) {
            throw new RuntimeException("单例模式禁止反射调用创建新对象！");
        }
    }

    public static SerializableSingleton getInstance() {
        return instance;
    }

    /**
     * 为什么序列化、反序列化能破坏单例模式？
     * 1. 单例模式要保证只有唯一的对象；
     * 2. 反序列化是通过反射创建新对象产生新对象，有新对象产生就违背单例模式
     * 因此，在反序列化过程中，需要对实现了Serializable接口的类进行特殊处理，保证单例模式不被破坏。
     *
     * 在ObjectInputStream类的readObject()方法中会根据类型（Object、Enum、Array、Exception等等）进行相应解析。
     * 具体解析过程是通过反射创建一个对象obj:
     *      - 如果不定义readResolve()方法，那么反射创建的对象obj，一定和序列化之前的对象不相同；
     *      - 如果定义了readResolve()方法，在反射（字符串"readResolve"在ObjectStreamClass类中有定义）执行该方法，
     * 并将该方法的返回值赋值给obj，最终返回obj。此时返回的obj就和序列化之前的对象完全相同；
     */
    public Object readResolve() {
        return instance;
    }
}
