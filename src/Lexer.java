package src;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {

    private static final List<Lexem> lexems = new ArrayList<>();

    static {
        lexems.add(new Lexem("DEL", Pattern.compile("^;$")));
        lexems.add(new Lexem("COM", Pattern.compile("^,$")));
        lexems.add(new Lexem("WHILE", Pattern.compile("^пока$")));
        lexems.add(new Lexem("FOR", Pattern.compile("^для$")));
        lexems.add(new Lexem("LL", Pattern.compile("^список$")));
        lexems.add(new Lexem("VAL_FUNC", Pattern.compile("^(взятьИз|взятьПосл|взятьПер|размер)$")));
        lexems.add(new Lexem("VOID_FUNC", Pattern.compile("^(добВ|добПер|добПосл|убр)$")));
        lexems.add(new Lexem("VAR", Pattern.compile("^([a-z]|[а-я])[([a-z]|[а-я])0-9]*$")));
        lexems.add(new Lexem("DIGIT", Pattern.compile("^0|([1-9][0-9]*)$")));
        lexems.add(new Lexem("COMP_OP", Pattern.compile("^>|<|>=|<=|==$")));
        lexems.add(new Lexem("ASSIGN_OP", Pattern.compile("^=$")));
        lexems.add(new Lexem("OP", Pattern.compile("^\\*|\\+|-|/$")));
        lexems.add(new Lexem("LBR", Pattern.compile("^\\($")));
        lexems.add(new Lexem("RBR", Pattern.compile("^\\)$")));
        lexems.add(new Lexem("LBC", Pattern.compile("^\\{$")));
        lexems.add(new Lexem("RBC", Pattern.compile("^}$")));
    }

    public List<Token> readTokens(String in) {
        List<Token> tokens = new LinkedList<>();
        char[] inChar = in.toCharArray();
        boolean matched = false;
        String value = "";
        int firstSym = 0;
        int j = 0;
        int delay = 10;

        for (int i = 1; i <= inChar.length; i++) {
            char[] buffer = new char[i - firstSym];
            System.arraycopy(inChar, firstSym, buffer, 0, i - firstSym);
            String currString = String.valueOf(buffer);
            if (currString.equals(" ") || currString.equals("\t")) {
                firstSym++;
                continue;
            }

            Matcher m = lexems.get(j).getPattern().matcher(currString);
            if (m.matches()) {
                matched = true;
                value = currString;
                if (i == inChar.length) {
                    tokens.add(new Token(lexems.get(j).getName(), value));
                }
            } else if (matched) {
                tokens.add(new Token(lexems.get(j).getName(), value));
                matched = false;
                firstSym = i - 1;
                i--;
                j = 0;
            } else if (i - firstSym <= delay && i < inChar.length) {
                continue;
            } else {
                j++;
                i = firstSym;
            }
        }
        return tokens;
    }
}


