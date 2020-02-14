package per.dqq.design.pattern.behavioral.iterator;

import java.util.List;

/**
 */
public class CourseIteratorImpl implements CourseIterator {
    private List<Course> courseList;
    private int position;

    public CourseIteratorImpl(List<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public Course nextCourse() {
        Course course = courseList.get(position);
        ++position;
        return course;
    }

    @Override
    public boolean isLastCourse() {
        return position == courseList.size();
    }
}
