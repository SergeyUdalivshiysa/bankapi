package exception;

public class NoSuchAccountException extends Exception{
    public NoSuchAccountException(String errorMessage){
        super(errorMessage);
    }
}
