package per.dqq.design.pattern.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CourseCatalog extends CatalogComponent {
    private List<CatalogComponent> items = new ArrayList<>();
    private String name;
    private Integer level;

    public CourseCatalog(String name, Integer level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public void add(CatalogComponent catalogComponent) {
        items.add(catalogComponent);
    }

    @Override
    public void remove(CatalogComponent catalogComponent) {
        items.remove(catalogComponent);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * 这里有一个结合多态递归的过程，程序运行期间：
     * 如果item是CourseCatalog类型，则递归调用print()方法
     * 如果item是Course类型，则直接使用item的print()方法输出
     */
    @Override
    public void print() {
        System.out.println(name);
        for (CatalogComponent item : items) {
            for (int i = 0; i < this.level; ++i) {
                System.out.print("    ");
            }
            item.print();
        }
    }
}
