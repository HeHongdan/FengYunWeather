package per.dqq.design.pattern.creational.singleton;

/**
 * 克隆破坏单例模式
 * 具体测试类为：prototype.clone包下的Test类
 * 解决方案：
 * 1. 单例类不实现Cloneable接口（本身单例和Cloneable接口语义上就有矛盾）
 * 2. clone()方法内部直接返回单例类实例instance
 *
 *
 */
public class CloneDestroySingleton implements Cloneable {
    private String name;
    private static CloneDestroySingleton instance = new CloneDestroySingleton();

    private CloneDestroySingleton() {
    }

    public static CloneDestroySingleton getInstance() {
        return instance;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        CloneDestroySingleton clone = (CloneDestroySingleton) super.clone();
        clone.name = this.name;
        return clone;
    }

    @Override
    public String toString() {
        return "CloneDestroySingleton{" +
                "name='" + name + '\'' +
                '}' +
                super.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
