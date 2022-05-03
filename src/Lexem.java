package src;

import java.util.regex.Pattern;

public class Lexem {
    private final String name;
    private final Pattern pattern;

    public Lexem(String name, Pattern pattern){
        this.name = name;
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
