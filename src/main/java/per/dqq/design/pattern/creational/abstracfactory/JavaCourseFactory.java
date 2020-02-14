package per.dqq.design.pattern.creational.abstracfactory;

/**
 * Java课程具体抽象工厂实现类
 *
 *
 */
public class JavaCourseFactory extends CourseFactory {
    @Override
    public Video getVideo() {
        return new JavaVideo();
    }

    @Override
    public Article getArticle() {
        return new JavaArticle();
    }
}
