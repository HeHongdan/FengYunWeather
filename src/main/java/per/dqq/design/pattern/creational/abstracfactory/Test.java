package per.dqq.design.pattern.creational.abstracfactory;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        CourseFactory javaCourseFactory = new JavaCourseFactory();
        Article javaArticle = javaCourseFactory.getArticle();
        Video javaVideo = javaCourseFactory.getVideo();
        javaArticle.produce();
        javaVideo.produce();

        CourseFactory pythonCourseFactory = new PythonCourseFactory();
        Article pythonArticle = pythonCourseFactory.getArticle();
        Video pythonVideo = pythonCourseFactory.getVideo();
        pythonArticle.produce();
        pythonVideo.produce();
    }
}
