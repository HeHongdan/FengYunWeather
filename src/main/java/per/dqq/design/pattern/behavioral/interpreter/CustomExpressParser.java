package per.dqq.design.pattern.behavioral.interpreter;

import java.util.Stack;

/**
 */
public class CustomExpressParser {

    public int parse(String str) {
        String[] strings = str.split(" ");
        Stack<NumberInterpreter> stack = new Stack<>();
        for (String string : strings) {
            if ("+".equals(string) || "*".equals(string)) {
                NumberInterpreter firstNum = stack.pop();
                NumberInterpreter secondNUm = stack.pop();
                if ("+".equals(string)) {
                    stack.add(new NumberInterpreter(firstNum.interpret() + secondNUm.interpret()));
                } else {
                    stack.add(new NumberInterpreter(firstNum.interpret() * secondNUm.interpret()));
                }
            } else {
                stack.push(new NumberInterpreter(string));
            }
        }
        return stack.pop().interpret();
    }
}
