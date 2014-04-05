package frontend;

/**
 * Created by Andrew Govorovsky on 15.03.14
 */
public enum UserStatus {

    USER_ADDED("User was added."),
    WAIT_USER_REG("Please, wait for registration"),
    WAIT_AUTH("Please, wait for authorization"),
    AUTHORIZED("Successful authorization"),
    USER_ALREADY_EXISTS("User already exists"),
    FAILED_AUTH("Wrong username or password"),
    NO_SUCH_USER_FOUND("No such user found"),
    SQL_ERROR("DB seems down. Try again"),
    EMPTY_DATA("Fill all fields"),
    OK("OK");

    private String message;

    public String getMessage() {
        return message;
    }

    private UserStatus(String message) {
        this.message = message;
    }

}
