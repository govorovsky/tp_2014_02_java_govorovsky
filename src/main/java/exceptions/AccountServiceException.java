package exceptions;

/**
 * Created by Andrew Govorovsky on 07.03.14
 */
public class AccountServiceException extends Exception {
    public AccountServiceException(String s) {
        super(s);
    }
}
