package per.dqq.design.pattern.creational.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 容器单例
 *
 *
 */
public class ContainerSingleton {
    private ContainerSingleton() {
    }

    private static Map<String, Object> map = new ConcurrentHashMap<>();

    public static void putInstance(String key, Object obj) {
        if ("".equals(key) && !map.containsKey(key)) {
            map.put(key, obj);
        }
    }

    public static Object getInstance(String key) {
        return map.get(key);
    }
}
