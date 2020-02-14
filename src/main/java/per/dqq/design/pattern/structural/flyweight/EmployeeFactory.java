package per.dqq.design.pattern.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EmployeeFactory {
    /**
     * 注意这里没有考虑HashMap线程不安全问题
     */
    private static final Map<String, Employee> EMPLOYEE_MAP = new HashMap<>();

    public static Employee getManager(String department) {
        Manager manager = (Manager) EMPLOYEE_MAP.get(department);
        if (manager == null) {
            manager = new Manager();
            manager.setDepartment(department);
            System.out.print("创建一个manager ");
            String reportContent = department + "开始汇报...";
            manager.setReportContent(reportContent);
            EMPLOYEE_MAP.put(department, manager);
        }
        return manager;
    }
}
