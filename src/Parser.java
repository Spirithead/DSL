package src;

import java.util.*;

public class Parser {
    private Token currToken;
    private ListIterator<Token> iterator;
    private List<Token> tokens;

    public Parser(List<Token> tokens) {
        iterator = tokens.listIterator();
        this.tokens = tokens;
    }

    private void nextToken() {
        currToken = iterator.next();
    }

    private void previousToken() {
        currToken = iterator.previous();
    }

    private boolean checkTerminal(String term) {
        try {
            nextToken();
        } catch (NoSuchElementException e) {
            return false;
        }

        if (currToken != null && currToken.getType().equals(term)) {
            return true;
        } else {
            previousToken();
            return false;
        }
    }

    private boolean checkValue() {
        if (!checkTerminal("VAR") && !checkTerminal("DIGIT")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkBcExprValue() {
        if (checkTerminal("LBR")) {
            if (checkExprValue()) {
                if (checkTerminal("RBR")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAddValue() {
        int count = 0;
        while (true) {
            if (checkTerminal("OP")) {
                if (checkValue() || checkBcExprValue() || checkExprValue()) {
                    count++;
                } else {
                    previousToken();
                    break;
                }
            } else break;
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    private boolean checkExprValue() {
        if (checkBcExprValue() && checkAddValue()) {
            return true;
        } else if (checkValue()) {
            checkAddValue();
            return true;
        }
        return false;
    }

    private boolean checkBody() {
        if (checkTerminal("LBC")) {
            int count = 0;
            while (true) {
                try {
                    checkExpr();
                    count++;
                } catch (SyntaxException e) {
                    break;
                }
            }
            if (checkTerminal("RBC")) {
                if (count > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkCompExpr() {
        if (checkValue()) {
            if (checkTerminal("COMP_OP")) {
                if (checkValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAssignExpr() {
        if (checkTerminal("VAR")) {
            if (checkTerminal("ASSIGN_OP")) {
                if (checkExprValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkWhileExpr() {
        if (checkTerminal("WHILE")) {
            if (checkTerminal("LBR")) {
                if (checkCompExpr()) {
                    if (checkTerminal("RBR")) {
                        if (checkBody()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkForExpr() {
        if (checkTerminal("FOR")) {
            if (checkTerminal("LBR")) {
                if (checkAssignExpr()) {
                    if (checkCompExpr()) {
                        if (checkAssignExpr()) {
                            if (checkTerminal("RBR")) {
                                if (checkBody()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void checkExpr() throws SyntaxException {
        if (!(checkAssignExpr() || checkWhileExpr() || checkForExpr())) {
            throw new SyntaxException();
        }
    }

    public void lang() {
        while (iterator.hasNext()) {
            try {
                checkExpr();
            } catch (SyntaxException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
        System.out.println(" ,\n=3");
    }

    private int priority(Token token) {
        return switch (token.getValue()) {
            case "*", "/" -> 3;
            case "+", "-" -> 2;
            case "=", ">", "<", ">=", "<=", "==" -> 1;
            default -> 0;
        };
    }

    public ArrayDeque<Token> polish() {
        ArrayDeque<Token> out = new ArrayDeque<>();
        ArrayDeque<Token> ops = new ArrayDeque<>();
        iterator = tokens.listIterator();
        while (iterator.hasNext()) {
            Token currToken = iterator.next();
            if(currToken.getType().equals("DEL")){
                while (!ops.isEmpty() && !ops.peek().getType().equals("LBR")) {
                    out.push(ops.pop());
                }
            }
            switch (currToken.getType()) {
                case "DIGIT", "VAR", "WHILE", "FOR", "LBC", "RBC" -> out.push(currToken);
                case "LBR" -> ops.push(currToken);
                case "RBR" -> {
                    while (!ops.peek().getType().equals("LBR")) {
                        out.push(ops.pop());
                    }
                    ops.pop();
                }
                case "OP", "ASSIGN_OP", "COMP_OP" -> {
                    while (!ops.isEmpty() && (priority(currToken) <= priority(ops.peek()))) {
                        out.push(ops.pop());
                    }
                    ops.push(currToken);
                }

            }
        }

        return out;
    }
}
