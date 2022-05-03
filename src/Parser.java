package src;

import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class Parser {
    private Token currToken;
    private final ListIterator<Token> iterator;

    public Parser(List<Token> tokens) {
        iterator = tokens.listIterator();
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

    private boolean checkAddValue(){
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
        if(checkBcExprValue() && checkAddValue()){
            return true;
        } else if(checkValue()){
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
            if(checkTerminal("RBC")){
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
        if(checkTerminal("WHILE")){
            if(checkTerminal("LBR")){
                if(checkCompExpr()){
                    if(checkTerminal("RBR")){
                        if(checkBody()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkForExpr() {
        if (checkTerminal("FOR")){
            if(checkTerminal("LBR")){
                if(checkAssignExpr()){
                    if(checkCompExpr()){
                        if(checkAssignExpr()){
                            if(checkTerminal("RBR")){
                                if(checkBody()){
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
            throw new SyntaxException(currToken.getLine() + ":" + currToken.getCharacter());
        }
    }

    public void lang() {
        while (iterator.hasNext()) {
            try {
                checkExpr();
            } catch (SyntaxException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        System.out.println(" ,\n=3");
    }
}
