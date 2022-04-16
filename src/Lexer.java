package src;

import java.util.*;
import java.util.regex.*;

public class Lexer {

    private static Map<String, Pattern> lexems = new HashMap<>();

    static {
        lexems.put("VAR", Pattern.compile("^[a-z][a-z0-9]*$"));
        lexems.put("DIGIT", Pattern.compile("^0|([1-9][0-9]*)$"));
        lexems.put("ASSIGN_OP", Pattern.compile("^=$"));
        lexems.put("OP",Pattern.compile("^\\*|\\+|-|/$"));
        lexems.put("LBR", Pattern.compile("^\\($"));
        lexems.put("RBR", Pattern.compile("^\\)$"));
    }

    public static void main(String[] args) {
        char[] in = "size=(a-10)+125".toCharArray();
        boolean matched = false;
        String value="";
        List<Token> tokens = new LinkedList<>();
        int firstSym = 0;
        int j=0;

        for(int i=1;i <= in.length;i++){
            char[] buffer = new char[i-firstSym];
            System.arraycopy(in,firstSym,buffer,0,i-firstSym);
            String currString = String.valueOf(buffer);
            Matcher m = lexems.get(lexems.keySet().toArray()[j].toString()).matcher(currString);
            if(m.matches()){
                matched=true;
                value=currString;
                if(i==in.length) tokens.add(new Token(lexems.keySet().toArray()[j].toString(),value));
            }
            else if(matched){
                tokens.add(new Token(lexems.keySet().toArray()[j].toString(),value));
                matched=false;
                firstSym = i-1;
                i--;
                j=0;
            }
            else {
                j++;
                i--;
            }
        }

        for (Token token: tokens) {
            System.out.println(token);
        }

    }
}

class Token {

    private String type;
    private String value;

    public Token(String type, String value){
        this.type = type;
        this.value = value;
    }


    @Override
    public String toString(){
        return "TOKEN[type=\"" + this.type + "\", value=\"" + this.value + "\"]";
    }

}
