package util;

/**
 * Created by Andrew Govorovsky on 04.04.14
 */
public class Result<T> {
    private T data;
    private UserState status;

    public Result(T result, UserState status) {
        this(status);
        this.data = result;
    }

    public Result(UserState status) {
        this.status = status;
    }

    public UserState getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }
}
