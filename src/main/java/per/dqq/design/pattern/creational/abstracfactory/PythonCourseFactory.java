package per.dqq.design.pattern.creational.abstracfactory;

/**
 * Python课程具体抽象工厂实现类
 *
 *
 */
public class PythonCourseFactory extends CourseFactory {
    @Override
    public Video getVideo() {
        return new PythonVideo();
    }

    @Override
    public Article getArticle() {
        return new PythonArticle();
    }
}
