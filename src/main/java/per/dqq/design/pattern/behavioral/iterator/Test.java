package per.dqq.design.pattern.behavioral.iterator;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Test {
    public static void main(String[] args) {
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course("Java"));
        courseList.add(new Course("C++"));
        courseList.add(new Course("Python"));
        courseList.add(new Course("Go"));
        CourseAggregate courseAggregate = new CourseAggregateImpl(courseList);
        CourseIterator iterator = courseAggregate.iterator();
        while (!iterator.isLastCourse()) {
            System.out.println(iterator.nextCourse().getName());
        }
    }
}
