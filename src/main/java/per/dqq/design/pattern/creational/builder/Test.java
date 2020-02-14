package per.dqq.design.pattern.creational.builder;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        Coach coach = new Coach();
        CourseBuilder courseBuilder = new JavaCourseBuilder();
        coach.setCb(courseBuilder);
        Course course = coach.buildCourse("设计模式", "视频", "ppt", "手记");
        System.out.println(course);
    }
}
