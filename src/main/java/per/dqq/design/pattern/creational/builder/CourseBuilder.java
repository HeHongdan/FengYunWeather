package per.dqq.design.pattern.creational.builder;

/**
 *
 */
public abstract class CourseBuilder {
    public abstract void buildName(String name);

    public abstract void buildVideo(String video);

    public abstract void buildPpt(String ppt);

    public abstract void buildArticle(String article);

    public abstract Course buildCourse();
}
