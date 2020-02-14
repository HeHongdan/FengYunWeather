package per.dqq.design.pattern.creational.simplefactory;

/**
 *
 */
public class JavaVideo extends Video {
    public JavaVideo() {
    }

    @Override
    public void produceVideo() {
        System.out.println("生产Java课程");
    }
}
