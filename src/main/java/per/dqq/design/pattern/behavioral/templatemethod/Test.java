package per.dqq.design.pattern.behavioral.templatemethod;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        AbstractCourse javaCourse = new JavaCourse();
        javaCourse.makeCourse();
        javaCourse.packageCourse();

        System.out.println("===================================");

        PythonCourse pythonCourse = new PythonCourse();
        pythonCourse.setNeedArticleFlag(true);
        pythonCourse.makeCourse();
        pythonCourse.packageCourse();
    }
}
