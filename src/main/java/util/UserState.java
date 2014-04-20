package util;

/**
 * Created by Andrew Govorovsky on 15.03.14
 */
public enum UserState {


    USER_ADDED(true, "User was added."),
    WAIT_USER_REG(true, "Please, wait for registration"),
    WAIT_AUTH(true, "Please, wait for authorization"),
    AUTHORIZED(true, "Successful authorization"),
    USER_ALREADY_EXISTS(false, "User already exists"),
    FAILED_AUTH(false, "Wrong username or password"),
    NO_SUCH_USER_FOUND(false, "No such user found"),
    SQL_ERROR(false, "DB seems down. Try again"),
    EMPTY_DATA(false, "Fill all fields"),
    OK(true, "OK");

    private String message;
    private boolean isSuccess;

    public String getMessage() {
        return message;
    }

    private UserState(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

}
