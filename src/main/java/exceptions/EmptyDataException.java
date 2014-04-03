package exceptions;

/**
 * Created by Andrew Govorovsky on 06.03.14
 */
public class EmptyDataException extends Exception {
    public EmptyDataException() {
        super(ExceptionMessages.EMPTY_DATA);
    }
}
