package per.dqq.design.pattern.behavioral.observer;

/**
 */
public class Test {
    public static void main(String[] args) {
        Course course = new Course("【Java设计模式】");
        Teacher sherman = new Teacher("Sherman");
        Teacher tanglei = new Teacher("Tanglei");

        Question question = new Question("stu1", "main函数怎么使用?");
        course.addObserver(sherman);
        course.addObserver(tanglei);
        course.productQuestion(question);
    }
}
