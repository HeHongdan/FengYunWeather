package per.dqq.design.pattern.behavioral.interpreter;

/**
 */
public class NumberInterpreter implements Interpreter {
    private int num;

    public NumberInterpreter(int num) {
        this.num = num;
    }

    public NumberInterpreter(String num) {
        this(Integer.parseInt(num));
    }

    @Override
    public int interpret() {
        return num;
    }

}
