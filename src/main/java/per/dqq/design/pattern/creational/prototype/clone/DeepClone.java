package per.dqq.design.pattern.creational.prototype.clone;

import java.util.Date;

/**
 * 深拷贝：不仅拷贝指针，还拷贝指针所指向地址的数据
 * 1. 在Java中clone()方法被设置为protected，只有实现了Cloneable接口的类对象
 * 才能调用clone()方法，String类型就不能调用clone()方法。
 * 2. 从String不可变类型也可以看出，要实现深拷贝更简单的方法是：将类定义为不可变（immutable）
 * 3. 对于既没有实现Cloneable接口，又是可变的类型，直接new出来即可：deepClone.sb = new StringBuilder(this.sb)
 * 4. 基本类型能够自动实现深度拷贝
 *
 *
 */
public class DeepClone implements Cloneable {
    private Date date;
    private StringBuilder sb;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        DeepClone deepClone = (DeepClone) super.clone();
        deepClone.date = (Date) deepClone.date.clone();
        /**
         * sb没有实现Cloneable接口，并且有是可变对象，直接new出来
         */
        deepClone.sb = new StringBuilder(this.sb);
        return deepClone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StringBuilder getSb() {
        return sb;
    }

    public void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public String toString() {
        return "DeepClone{" +
                "date=" + date +
                ", sb=" + sb +
                '}' +
                super.toString();
    }
}
