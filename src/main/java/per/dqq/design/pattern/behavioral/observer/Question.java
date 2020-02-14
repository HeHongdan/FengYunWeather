package per.dqq.design.pattern.behavioral.observer;

/**
 */
public class Question {
    private String userName;
    private String question;

    public Question(String userName, String question) {
        this.userName = userName;
        this.question = question;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
