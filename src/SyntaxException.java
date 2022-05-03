package src;

public class SyntaxException extends Exception{
    public SyntaxException(String message){
        super("Unexpected item at " + message);
    }
}
