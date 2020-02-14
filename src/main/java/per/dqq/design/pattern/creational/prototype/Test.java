package per.dqq.design.pattern.creational.prototype;

import java.util.ArrayList;

/**
 * for循环中需要创建大量ComplexObj对象，但是该对象的创建代价高
 * 可以使用clone()方法完成拷贝
 *
 *
 */
public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {
        ComplexObj complexObj = new ComplexObj();
        complexObj.setCommon("common part");
        System.out.println(complexObj);
        for (int i = 0; i < 10; ++i) {
            /**
             * Clone的全部都是不同对象
             */
            ComplexObj tmp = (ComplexObj) complexObj.clone();
            tmp.setName("name-" + i);
            tmp.setRef("ref-" + i);
            System.out.println(tmp);
        }
//        ArrayList是深拷贝
//        ArrayList<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        ArrayList<Integer> clone = (ArrayList<Integer>) list.clone();
//        list.forEach(System.out::print);
//        System.out.println();
//        clone.forEach(System.out::print);
//        list.set(0, 100);
//        clone.set(2, 300);
//
//        System.out.println();
//
//        list.forEach(System.out::print);
//        System.out.println();
//        clone.forEach(System.out::print);
    }
}
