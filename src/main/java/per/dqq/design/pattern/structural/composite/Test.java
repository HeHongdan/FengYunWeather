package per.dqq.design.pattern.structural.composite;

/**
 *
 */
public class Test {
    public static void main(String[] args) {
        CatalogComponent mainCatalog = new CourseCatalog("主目录", 1);
        Course windowsCourse = new Course("windows操作系统", 10.0);
        Course linuxCourse = new Course("linux操作系统", 10.0);

        CatalogComponent javaCatalog = new CourseCatalog("Java课程目录", 2);
        Course mallCourse1 = new Course("Java电商课程一期", 20.0);
        Course mallCourse2 = new Course("Java电商课程二期", 30.0);
        Course designPattern = new Course("Java设计模式", 40.0);
        javaCatalog.add(mallCourse1);
        javaCatalog.add(mallCourse2);
        javaCatalog.add(designPattern);
        mainCatalog.add(windowsCourse);
        mainCatalog.add(linuxCourse);
        mainCatalog.add(javaCatalog);
        mainCatalog.print();
    }
}
