package resource;

/**
 * Created by Andrew Govorovsky on 24.04.14
 */
public class TemplatesResource implements Resource {

    public String AUTH_TPL;
    public String REGISTER_TPL;
    public String MAIN_TPL;
    public String TIMER_TPL;
    public String USER_TPL;


    public String getAUTH_TPL() {
        return AUTH_TPL;
    }

    public String getREGISTER_TPL() {
        return REGISTER_TPL;
    }

    public String getMAIN_TPL() {
        return MAIN_TPL;
    }

    public String getTIMER_TPL() {
        return TIMER_TPL;
    }

    public String getUSER_TPL() {
        return USER_TPL;
    }
}
