package per.dqq.design.pattern.structural.composite;

/**
 *
 */
public class Course extends CatalogComponent {
    private String name;
    private Double price;


    public Course(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    @Override
    public void print() {
        System.out.println("课程名称:" + name + " 课程价格:" + price);
    }
}
