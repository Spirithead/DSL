package src;

public class Token {

    private final String type;
    private String value;
    private int intValue;

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
    }
    public Token(String type, int value) {
        this.type = type;
        this.intValue = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TOKEN[type=\"" + this.type + "\", value=\"" + this.value + "\"]";
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}
