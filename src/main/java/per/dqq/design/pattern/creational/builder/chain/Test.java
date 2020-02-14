package per.dqq.design.pattern.creational.builder.chain;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        /**
         * Course的ext属性使用了默认值，没有进行build赋值
         */
        Course javaCourse = new Course.JavaCourseBuilder()
                .buildName("设计模式")
                .buildVideo("视频")
                .buildPpt("ppt")
                .buildArticle("手记")
                .buildQa("question&answer")
                .build();
        System.out.println(javaCourse);
    }
}
