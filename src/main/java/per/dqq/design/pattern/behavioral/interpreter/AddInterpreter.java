package per.dqq.design.pattern.behavioral.interpreter;

/**
 */
public class AddInterpreter implements Interpreter {
    private Interpreter firstInterpreter;
    private Interpreter secondInterpreter;

    public AddInterpreter(Interpreter firstInterpreter, Interpreter secondInterpreter) {
        this.firstInterpreter = firstInterpreter;
        this.secondInterpreter = secondInterpreter;
    }

    @Override
    public int interpret() {
        return firstInterpreter.interpret() + secondInterpreter.interpret();
    }

    @Override
    public String toString() {
        return "+";
    }
}
