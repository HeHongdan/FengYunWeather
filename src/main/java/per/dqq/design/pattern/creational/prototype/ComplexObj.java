package per.dqq.design.pattern.creational.prototype;

/**
 * 假设该类是一个非常复杂的类，new出来该对象成本非常高
 *
 *
 */
public class ComplexObj implements Cloneable {
    private String name;
    private String ref;

    public String getCommon() {
        return common;
    }

    public void setCommon(String common) {
        this.common = common;
    }

    private String common;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "ComplexObj{" +
                "name='" + name + '\'' +
                ", ref='" + ref + '\'' +
                ", common='" + common + '\'' +
                '}' +
                super.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        /**
         * 使用的是浅拷贝
         */
        return super.clone();
    }
}
