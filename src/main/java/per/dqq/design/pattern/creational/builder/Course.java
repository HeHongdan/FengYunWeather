package per.dqq.design.pattern.creational.builder;

/**
 *
 */
public class Course {
    private String name;
    private String video;
    private String ppt;
    private String article;

    public void setName(String name) {
        this.name = name;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setPpt(String ppt) {
        this.ppt = ppt;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", video='" + video + '\'' +
                ", ppt='" + ppt + '\'' +
                ", article='" + article + '\'' +
                '}';
    }
}
