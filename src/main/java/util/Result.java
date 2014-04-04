package util;

/**
 * Created by Andrew Govorovsky on 04.04.14
 */
public class Result<T> {
    private T result;
    private String status;

    public Result(T result) {
        this.result = result;
    }

    public Result(T result, String status) {
        this(result);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }
}
