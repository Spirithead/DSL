package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = "src\\source.txt";
        String line;
        StringBuilder in = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            in.append(line);
        }
        br.close();
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.readTokens(in.toString());

        //System.out.println(tokens);
        Parser parser = new Parser(tokens);
        //parser.lang();
        ArrayDeque<Token> polish = parser.polish();
        //System.out.println(polish);
        StackMachine stackMachine = new StackMachine(polish);
        stackMachine.execute();
        System.out.println(stackMachine.getVars());
    }
}
