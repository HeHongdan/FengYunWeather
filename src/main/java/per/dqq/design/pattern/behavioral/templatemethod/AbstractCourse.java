package per.dqq.design.pattern.behavioral.templatemethod;

/**
 *
 */
public abstract class AbstractCourse {
    protected final void makeCourse() {
        makePpt();
        makeVideo();
        if (needArticle()) {
            makeArticle();
        }
    }

    final void makePpt() {
        System.out.println("制作ppt");
    }

    final void makeVideo() {
        System.out.println("制作video");
    }

    /**
     * 钩子方法，开放给子类
     */
    protected boolean needArticle() {
        return false;
    }

    final void makeArticle() {
        System.out.println("制作手记");
    }

    protected abstract void packageCourse();
}
