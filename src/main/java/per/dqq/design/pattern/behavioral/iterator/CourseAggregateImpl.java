package per.dqq.design.pattern.behavioral.iterator;

import java.util.List;

/**
 */
public class CourseAggregateImpl implements CourseAggregate {
    private List<Course> courseList;

    public CourseAggregateImpl(List<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public void addCourse(Course course) {
        courseList.add(course);
    }

    @Override
    public void removeCourse(Course course) {
        courseList.remove(course);
    }

    @Override
    public CourseIterator iterator() {
        return new CourseIteratorImpl(courseList);
    }
}
