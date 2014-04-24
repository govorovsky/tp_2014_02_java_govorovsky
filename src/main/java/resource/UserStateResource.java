package resource;

/**
 * Created by Andrew Govorovsky on 24.04.14
 */
public class UserStateResource implements Resource {
    private String USER_ADDED;
    private String WAIT_USER_REG;
    private String WAIT_AUTH;
    private String AUTHORIZED;
    private String USER_ALREADY_EXISTS;
    private String FAILED_AUTH;
    private String NO_SUCH_USER_FOUND;
    private String SQL_ERROR;
    private String EMPTY_DATA;
    private String OK;

    public String getUSER_ADDED() {
        return USER_ADDED;
    }

    public String getWAIT_USER_REG() {
        return WAIT_USER_REG;
    }

    public String getWAIT_AUTH() {
        return WAIT_AUTH;
    }

    public String getAUTHORIZED() {
        return AUTHORIZED;
    }

    public String getUSER_ALREADY_EXISTS() {
        return USER_ALREADY_EXISTS;
    }

    public String getFAILED_AUTH() {
        return FAILED_AUTH;
    }

    public String getNO_SUCH_USER_FOUND() {
        return NO_SUCH_USER_FOUND;
    }

    public String getSQL_ERROR() {
        return SQL_ERROR;
    }

    public String getEMPTY_DATA() {
        return EMPTY_DATA;
    }

    public String getOK() {
        return OK;
    }
}
