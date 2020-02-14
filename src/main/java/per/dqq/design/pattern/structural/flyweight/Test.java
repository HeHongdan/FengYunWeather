package per.dqq.design.pattern.structural.flyweight;

import java.util.Random;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        String[] departments = {"RD", "PM", "BD", "QA"};
        for (int i = 0; i < 10; ++i) {
            Employee manager = EmployeeFactory.getManager(departments[(int) (Math.random() * departments.length)]);
            manager.report();
        }
    }
}
