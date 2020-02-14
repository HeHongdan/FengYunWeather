package per.dqq.design.pattern.behavioral.observer;

import java.util.Observable;

/**
 */
public class Course extends Observable {
    private String courseName;

    public Course(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void productQuestion(Question question) {
        System.out.println(question.getUserName() + "在" + courseName + "提交了一个问题");
        setChanged();
        /**
         * this和参数question传递给所有观察者的update(Observable o, Object arg)方法入参
         */
        notifyObservers(question);
    }
}
