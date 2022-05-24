package src;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class StackMachine {
    ArrayDeque<Token> in;
    static Map<String, Integer> vars = new HashMap<>();

    public StackMachine(ArrayDeque<Token> in) {
        this.in = in;
    }

    public void execute() {
        ArrayDeque<Token> temp = new ArrayDeque<>(in);
        while (!temp.isEmpty()) {
            switch (temp.getLast().getType()) {
                case "VAR" -> {
                    String varName = temp.getLast().getValue();
                    temp.removeLast();
                    int value = calculate(temp);
                    temp.removeLast();
                    varAssign(varName, value);
                }
                case "WHILE" -> {
                    temp.removeLast();
                    ArrayDeque<Token> condition = new ArrayDeque<>();
                    while (!temp.getLast().getType().equals("LBC")) {
                        condition.push(temp.removeLast());
                    }
                    temp.removeLast();
                    ArrayDeque<Token> win = new ArrayDeque<>();
                    while (!temp.getLast().getType().equals("RBC")) {
                        win.push(temp.removeLast());
                    }
                    temp.removeLast();
                    StackMachine whileMachine = new StackMachine(win);
                    while (checkCondition(condition)) {
                        whileMachine.execute();
                    }
                }
                case "FOR" -> {
                    temp.removeLast();
                    ArrayDeque<Token> condition = new ArrayDeque<>();
                    while (!temp.getLast().getType().equals("COMP_OP")) {
                        condition.push(temp.removeLast());
                    }
                    condition.push(temp.removeLast());
                    ArrayDeque<Token> step = new ArrayDeque<>();
                    while (!temp.getLast().getType().equals("LBC")) {
                        step.push(temp.removeLast());
                    }
                    temp.removeLast();
                    ArrayDeque<Token> fin = new ArrayDeque<>();
                    while (!temp.getLast().getType().equals("RBC")) {
                        fin.push(temp.removeLast());
                    }
                    temp.removeLast();
                    StackMachine forMachine = new StackMachine(fin);
                    StackMachine stepMachine = new StackMachine(step);
                    while (checkCondition(condition)) {
                        forMachine.execute();
                        stepMachine.execute();
                    }
                }
            }
        }
    }

    private int varEval(String name) {
        if (vars.get(name) != null) {
            return vars.get(name);
        } else {
            vars.put(name, 0);
            return 0;
        }
    }

    private void varAssign(String name, int value) {
        if (vars.get(name) != null) {
            vars.replace(name, value);
        } else {
            vars.put(name, value);
        }
    }

    private int calculate(ArrayDeque<Token> in) {
        int res = 0;
        ArrayDeque<Integer> workspace = new ArrayDeque<>();
        while (!in.isEmpty() &&
                (in.getLast().getType().equals("VAR")
                        || in.getLast().getType().equals("DIGIT") || in.getLast().getType().equals("OP"))) {
            if (in.getLast().getType().equals("VAR")) {
                workspace.push(varEval(in.removeLast().getValue()));
            } else if (in.getLast().getType().equals("DIGIT")) {
                workspace.push(Integer.parseInt(in.removeLast().getValue()));
            } else {
                switch (in.removeLast().getValue()) {
                    case "+" -> res = workspace.pop() + workspace.pop();
                    case "*" -> res = workspace.pop() * workspace.pop();
                    case "-" -> {
                        int o1 = workspace.pop();
                        int o2 = workspace.pop();
                        res = o2 - o1;
                    }
                    case "/" -> {
                        int o1 = workspace.pop();
                        int o2 = workspace.pop();
                        res = o2 / o1;
                    }
                }
                workspace.push(res);
            }
        }
        return workspace.pop();
    }

    private boolean checkCondition(ArrayDeque<Token> in) {
        ArrayDeque<Token> temp = new ArrayDeque<>(in);
        boolean res = false;
        int o1, o2;
        if (temp.getLast().getType().equals("VAR")) {
            o1 = varEval(temp.removeLast().getValue());
        } else {
            o1 = Integer.parseInt(temp.removeLast().getValue());
        }
        if (temp.getLast().getType().equals("VAR")) {
            o2 = varEval(temp.removeLast().getValue());
        } else {
            o2 = Integer.parseInt(temp.removeLast().getValue());
        }
        switch (temp.getLast().getValue()) {
            case ">" -> {
                return (o1 > o2);
            }
            case "<" -> {
                return (o1 < o2);
            }
            case ">=" -> {
                return (o1 >= o2);
            }
            case "<=" -> {
                return (o1 <= o2);
            }
            case "==" -> {
                return (o1 == o2);
            }
        }
        return res;
    }

    public Map<String, Integer> getVars() {
        return vars;
    }
}
