package per.dqq.design.pattern.creational.singleton;

import java.io.*;
import java.lang.reflect.Constructor;

/**
 *
 */
public class SerializableSingletonTest {
    public static void main(String[] args) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("object.xml"));
        SerializableSingleton originalInstance = SerializableSingleton.getInstance();
        oos.writeObject(originalInstance);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.xml"));
        Object newInstance = ois.readObject();
        System.out.println(originalInstance);
        System.out.println(newInstance);
        System.out.println(originalInstance == newInstance);

        /**
         * 尝试通过反射创建单例对象
         * 加入单例防御性代码之后直接抛出异常
         */
        Class<SerializableSingleton> c = SerializableSingleton.class;
        Constructor<SerializableSingleton> ctor = c.getDeclaredConstructor();
        ctor.setAccessible(true);
        SerializableSingleton obj = ctor.newInstance();
        System.out.println(obj);
    }
}
