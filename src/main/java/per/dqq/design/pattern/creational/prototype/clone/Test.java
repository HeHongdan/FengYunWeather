package per.dqq.design.pattern.creational.prototype.clone;

import per.dqq.design.pattern.creational.singleton.CloneDestroySingleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 测试浅拷贝和深拷贝
 *
 *
 */
public class Test {
    public static void main(String[] args) throws CloneNotSupportedException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        /**
         * 测试浅拷贝
         */
        ShallowClone shallowObj1 = new ShallowClone(new Date(System.currentTimeMillis()));
        ShallowClone shallowObj2 = (ShallowClone) shallowObj1.clone();
        System.out.println(shallowObj1);
        System.out.println(shallowObj2);

        // !shallowObj1.setDae(new Date(1L));
        shallowObj1.getDate().setTime(1L);
        System.out.println(shallowObj1);
        System.out.println(shallowObj2);

        System.out.println();

        /**
         * 测试深拷贝
         */
        DeepClone deepObj1 = new DeepClone();
        deepObj1.setDate(new Date(0L));
        deepObj1.setSb(new StringBuilder("sb"));
        DeepClone deepObj2 = (DeepClone) deepObj1.clone();
        System.out.println(deepObj1);
        System.out.println(deepObj2);

        deepObj1.setSb(new StringBuilder("new sb"));
        deepObj2.getDate().setTime(System.currentTimeMillis());
        System.out.println(deepObj1);
        System.out.println(deepObj2);

        /**
         * 克隆破坏单例模式
         */
        CloneDestroySingleton instance1 = CloneDestroySingleton.getInstance();
        instance1.setName("sherman");
        Class<CloneDestroySingleton> c = CloneDestroySingleton.class;
        Method clone = c.getDeclaredMethod("clone");
        clone.setAccessible(true);
        CloneDestroySingleton instance2 = (CloneDestroySingleton) clone.invoke(instance1);
        System.out.println(instance1);
        System.out.println(instance2);
    }
}
