package per.dqq.design.pattern.creational.singleton;

/**
 * 枚举类单例模式通过jad反编译工具可以看出：
 * 1. 相当于static代码块中恶汉式单例模式
 * 2. 枚举类被声明为final
 * 3. 如果有abstract方法，枚举类又会被声明为abstract
 * 4. abstract方法的实现会被放入到static代码块中
 *
 *
 */
public enum EnumSingleton {
    instance {
        protected void hello() {
            System.out.println("hello");
        }
    };

    protected abstract void hello();

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private Object data;

    public static EnumSingleton getInstance() {
        return instance;
    }
}
