package src;

import java.util.*;

public class StackMachine {
    ArrayDeque<Token> in;
    static Map<String, Integer> varsValues = new HashMap<>();
    static Map<String, String> varsTypes = new HashMap<>();
    static Map<String, LL<Token>> lls = new HashMap<>();

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
                    int skip = 1;
                    temp.removeLast();
                    ArrayDeque<Token> condition = new ArrayDeque<>();
                    while (!temp.getLast().getType().equals("LBC")) {
                        condition.push(temp.removeLast());
                    }
                    temp.removeLast();
                    ArrayDeque<Token> win = new ArrayDeque<>();
                    do {
                        if (temp.getLast().getType().equals("LBC")) {
                            skip++;
                        }
                        if (temp.getLast().getType().equals("RBC")) {
                            skip--;
                        }
                        if (skip != 0) {
                            win.push(temp.removeLast());
                        }
                    }
                    while (!temp.getLast().getType().equals("RBC") || skip != 0);
                    temp.removeLast();
                    StackMachine whileMachine = new StackMachine(win);
                    while (checkCondition(condition)) {
                        whileMachine.execute();
                    }
                }
                case "FOR" -> {
                    int skip = 1;
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
                    do {
                        if (temp.getLast().getType().equals("LBC")) {
                            skip++;
                        }
                        if (temp.getLast().getType().equals("RBC")) {
                            skip--;
                        }
                        if (skip != 0) {
                            fin.push(temp.removeLast());
                        }
                    }
                    while (!temp.getLast().getType().equals("RBC") || skip != 0);
                    temp.removeLast();
                    StackMachine forMachine = new StackMachine(fin);
                    StackMachine stepMachine = new StackMachine(step);
                    while (checkCondition(condition)) {
                        forMachine.execute();
                        stepMachine.execute();
                    }
                }
                case "LL" -> {
                    temp.removeLast();
                    String llName = temp.getLast().getValue();
                    LL<Token> ll = llEval(llName);
                    temp.removeLast();
                    temp.removeLast();
                    while (!temp.getLast().getType().equals("RBC")) {
                        ll.addLast(temp.removeLast());
                    }
                    temp.removeLast();
                    temp.removeLast();
                }
                case "VOID_FUNC" -> funcExe(temp);
            }
        }
    }

    private int varEval(String name) {
        if (varsValues.get(name) != null) {
            return varsValues.get(name);
        } else {
            varsValues.put(name, 0);
            varsTypes.put(name, "int");
            return 0;
        }
    }

    private int valEval(String type, String value) {
        if (type.equals("VAR")) {
            return varEval(value);
        } else {
            return Integer.parseInt(value);
        }
    }

    private LL<Token> llEval(String name) {
        if (lls.get(name) != null) {
            return lls.get(name);
        } else if (varsTypes.get(name) == null) {
            LL<Token> ll = new LL<>();
            lls.put(name, ll);
            varsTypes.put(name, "ll");
            return ll;
        } else {
            System.out.println("This var already exists");
            System.exit(1);
        }
        return null;
    }

    private void varAssign(String name, int value) {
        if (varsValues.get(name) != null) {
            varsValues.replace(name, value);
        } else {
            varsValues.put(name, value);
            varsTypes.put(name, "int");
        }
    }

    private int calculate(ArrayDeque<Token> in) {
        int res = 0;
        ArrayDeque<Integer> workspace = new ArrayDeque<>();
        while (!in.isEmpty() &&
                (in.getLast().getType().equals("VAR")
                        || in.getLast().getType().equals("DIGIT") || in.getLast().getType().equals("OP"))
                || in.getLast().getType().equals("VAL_FUNC")) {
            switch (in.getLast().getType()) {
                case "VAR" -> workspace.push(varEval(in.removeLast().getValue()));
                case "DIGIT" -> workspace.push(Integer.parseInt(in.removeLast().getValue()));
                case "VAL_FUNC" -> workspace.push(funcExe(in));
                default -> {
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

    private int funcExe(ArrayDeque<Token> in) {
        String func = in.removeLast().getValue();
        in.removeLast();
        String target = in.removeLast().getValue();
        LL<Token> params = new LL<>();
        while (!in.getLast().getType().equals("RBC")) {
            params.addLast(in.removeLast());
        }
        in.removeLast();
        LL<Token> ll = llEval(target);
        switch (func) {
            case "взятьИз" -> {
                int i = valEval(params.get(0).getType(), params.get(0).getValue());
                Token token = ll.get(i);
                return valEval(token.getType(), token.getValue());
            }
            case "взятьПосл" -> {
                Token token = ll.getLast();
                return valEval(token.getType(), token.getValue());
            }
            case "взятьПер" -> {
                Token token = ll.getFirst();
                return valEval(token.getType(), token.getValue());
            }
            case "добВ" -> {
                ll.addAt(valEval(params.get(0).getType(), params.get(0).getValue()), params.get(1));
            }
            case "добПер" -> {
                for (int i = 0; i < params.getSize(); i++) {
                    ll.addFirst(params.get(i));
                }

            }
            case "добПосл" -> {
                for (int i = 0; i < params.getSize(); i++) {
                    ll.addLast(params.get(i));
                }
            }
            case "убр" -> {
                ll.remove(valEval(params.get(0).getType(), params.get(0).getValue()));
            }
            case "размер" -> {
                return ll.getSize();
            }
        }
        return 0;
    }

    public Map<String, Integer> getVars() {
        return varsValues;
    }

    public Map<String, LL<Token>> getLls() {
        return lls;
    }
}
