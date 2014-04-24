package frontend;

import resource.ResourceSystem;
import resource.URLResource;

/**
 * Created by Andrew Govorovsky on 06.03.14
 */
public final class Pages {

    private static final String RES_FILE = "url.xml";
    private static final URLResource URLR = (URLResource) ResourceSystem.getInstance().getResource(RES_FILE);

    private Pages() {
    }

    public static final String AUTH_PAGE = URLR.getAUTH_PAGE();
    public static final String REG_PAGE = URLR.getREG_PAGE();
    public static final String MAIN_PAGE = URLR.getMAIN_PAGE();
    public static final String TIMER_PAGE = URLR.getTIMER_PAGE();
    public static final String QUIT_PAGE = URLR.getQUIT_PAGE();
}
