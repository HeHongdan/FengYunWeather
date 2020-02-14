package per.dqq.design.pattern.creational.simplefactory;

/**
 * 简单工厂类源码使用：
 * 1. Calendar -> createCalendar()
 * 2. LoggerFactory(log4j) -> getLogger()方法
 *
 *
 */
public class Test {
    // v1 业务组件直接依赖具体实例对象
//    public static void main(String[] args) {
//        Video javaVideo = new JavaVideo();
//        javaVideo.produceVideo();
//        PythonVideo pythonVideo = new PythonVideo();
//        pythonVideo.produceVideo();
//    }

    // v2 业务组件直接以来VideoFactory，由VideoFactory负责各种实例创建
//    public static void main(String[] args) {
//        VideoFactory videoFactory = new VideoFactory();
//        Video javaVideo = videoFactory.getVideo("java");
//        if (javaVideo != null) {
//            javaVideo.produceVideo();
//        }
//    }

    /**
     * 业务组件以来VideoFactory，并结合反射
     */
    public static void main(String[] args) {
        VideoFactory videoFactory = new VideoFactory();
        Video javaCourse = videoFactory.getVideo(JavaVideo.class);
        if (javaCourse != null) {
            javaCourse.produceVideo();
        }
    }
}
