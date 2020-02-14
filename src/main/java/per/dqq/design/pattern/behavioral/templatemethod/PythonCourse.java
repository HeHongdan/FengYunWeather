package per.dqq.design.pattern.behavioral.templatemethod;

/**
 *
 */
public class PythonCourse extends AbstractCourse {
    private boolean needArticleFlag = false;

    @Override
    protected void packageCourse() {
        System.out.println("开始打包Python课程");
    }

    public boolean isNeedArticleFlag() {
        return needArticleFlag;
    }

    /**
     * 将钩子方法开放给客户端
     */
    public void setNeedArticleFlag(boolean needArticleFlag) {
        this.needArticleFlag = needArticleFlag;
    }

    @Override
    protected boolean needArticle() {
        return needArticleFlag;
    }
}
