package src;

public class Token {

    private final String type;
    private final String value;
    private final int character;
    private final int line;

    public Token(String type, String value, int position, int line) {
        this.type = type;
        this.value = value;
        this.character = position;
        this.line = line;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getCharacter() {
        return character;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "TOKEN[type=\"" + this.type + "\", value=\"" + this.value + "\", line=\""
                + this.line + "\", character=\"" + this.character + "\"]";
    }
}
