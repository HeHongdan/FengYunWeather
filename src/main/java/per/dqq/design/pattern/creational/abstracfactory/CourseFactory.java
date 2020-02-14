package per.dqq.design.pattern.creational.abstracfactory;

/**
 * 抽象课程工厂，针对同一产品族，该产品族下有两个产品等级：Video & Article
 *
 *
 */
public abstract class CourseFactory {
    public abstract Video getVideo();

    public abstract Article getArticle();
}
