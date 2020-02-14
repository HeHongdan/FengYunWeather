package per.dqq.design.pattern.creational.builder.chain;

/**
 * 建造者使用：静态内部类 & return this完成链式调用
 *
 *
 */
public class Course {
    private String name;
    private String video;
    private String ppt;
    private String article;
    private String qa;
    private String ext;

    public Course(JavaCourseBuilder jcb) {
        this.name = jcb.name;
        this.video = jcb.video;
        this.ppt = jcb.ppt;
        this.article = jcb.article;
        this.qa = jcb.qa;
        this.ext = jcb.ext;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", video='" + video + '\'' +
                ", ppt='" + ppt + '\'' +
                ", article='" + article + '\'' +
                ", qa='" + qa + '\'' +
                ", ext='" + ext + '\'' +
                '}';
    }

    public static class JavaCourseBuilder {
        private String name;
        private String video;
        private String ppt;
        private String article;
        private String qa;
        private String ext = "placeholder";

        public JavaCourseBuilder buildName(String name) {
            this.name = name;
            return this;
        }

        public JavaCourseBuilder buildVideo(String video) {
            this.video = video;
            return this;
        }

        public JavaCourseBuilder buildPpt(String ppt) {
            this.ppt = ppt;
            return this;
        }

        public JavaCourseBuilder buildArticle(String article) {
            this.article = article;
            return this;
        }

        public JavaCourseBuilder buildQa(String qa) {
            this.qa = qa;
            return this;
        }

        public JavaCourseBuilder buildExt(String ext) {
            this.ext = ext;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }
}
