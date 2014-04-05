package util;

import frontend.UserStatus;

/**
 * Created by Andrew Govorovsky on 04.04.14
 */
public class Result<T> {
    private T result;
    private UserStatus status;

    public Result(T result) {
        this.result = result;
    }

    public Result(T result, UserStatus status) {
        this(result);
        this.status = status;
    }

    public UserStatus getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }
}
