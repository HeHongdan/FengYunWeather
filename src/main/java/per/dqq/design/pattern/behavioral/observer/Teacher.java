package per.dqq.design.pattern.behavioral.observer;

import java.util.Observable;
import java.util.Observer;

/**
 */
public class Teacher implements Observer {
    private String name;

    public Teacher(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        Course course = (Course)o;
        Question question = (Question)arg;
        System.out.println(name + "在" + course.getCourseName() + "上接受到一个问题：" + question.getQuestion());
    }
}
