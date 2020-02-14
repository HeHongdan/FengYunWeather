package per.dqq.design.pattern.creational.builder;

/**
 *
 */
public class JavaCourseBuilder extends CourseBuilder {
    private Course course;

    public JavaCourseBuilder() {
        course = new Course();
    }

    @Override
    public void buildName(String name) {
        course.setName(name);
    }

    @Override
    public void buildVideo(String video) {
        course.setVideo(video);
    }

    @Override
    public void buildPpt(String ppt) {
        course.setPpt(ppt);
    }

    @Override
    public void buildArticle(String article) {
        course.setArticle(article);
    }

    @Override
    public Course buildCourse() {
        return course;
    }
}
