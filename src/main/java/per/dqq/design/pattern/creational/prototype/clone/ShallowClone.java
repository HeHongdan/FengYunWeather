package per.dqq.design.pattern.creational.prototype.clone;

import java.util.Date;

/**
 * 浅拷贝，只拷贝指针，不拷贝指针所指向地址的内容
 *
 *
 */
public class ShallowClone implements Cloneable {
    private Date date;

    public ShallowClone(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ShallowClone{" +
                "date='" + date + '\'' +
                '}' +
                super.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
