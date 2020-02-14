package per.dqq.design.pattern.creational.builder;

/**
 * 建造视频的教练
 * 实际建造者对于产品的建造步骤是由coach进行控制的
 *
 *
 */
public class Coach {
    private CourseBuilder cb;

    public void setCb(CourseBuilder cb) {
        this.cb = cb;
    }

    public Course buildCourse(String name, String video, String ppt, String article) {
        cb.buildName(name);
        cb.buildVideo(video);
        cb.buildPpt(ppt);
        cb.buildArticle(article);
        return cb.buildCourse();
    }
}
