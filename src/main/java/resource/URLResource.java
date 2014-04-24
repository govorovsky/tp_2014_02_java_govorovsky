package resource;

/**
 * Created by Andrew Govorovsky on 24.04.14
 */
public class URLResource implements Resource {
    public String AUTH_PAGE;
    private String REG_PAGE;
    private String MAIN_PAGE;
    private String TIMER_PAGE;
    private String QUIT_PAGE;

    public String getAUTH_PAGE() {
        return AUTH_PAGE;
    }

    public String getREG_PAGE() {
        return REG_PAGE;
    }

    public String getMAIN_PAGE() {
        return MAIN_PAGE;
    }

    public String getTIMER_PAGE() {
        return TIMER_PAGE;
    }

    public String getQUIT_PAGE() {
        return QUIT_PAGE;
    }
}
