package per.dqq.design.pattern.creational.singleton;

import java.lang.reflect.Constructor;

/**
 *
 */
public class EnumSingletonTest {
    public static void main(String[] args) throws Exception {
        /**
         * 抛出IllegalArgumentException：Cannot reflectively create enum objects
         * 该异常由Constructor类抛出，即Constructor类会对Enum进行一些防护措施
         */
//        EnumSingleton obj1 = EnumSingleton.getInstance();
//        Class<EnumSingleton> c = EnumSingleton.class;
//        Constructor<EnumSingleton> ctor = c.getDeclaredConstructor(String.class, int.class);
//        ctor.setAccessible(true);
//        EnumSingleton obj2 = ctor.newInstance("sherman", 1);
//
//        System.out.println(obj1);
//        System.out.println(obj2);
//        System.out.println(obj1 == obj2);

        EnumSingleton instance = EnumSingleton.getInstance();
        instance.hello();
    }
}
