package frontend;

import resource.ResourceSystem;
import resource.TemplatesResource;

/**
 * Created by Andrew Govorovsky on 06.03.14
 */
public final class Templates {

    private static final String RES_FILE = "templates.xml";
    private static final TemplatesResource TR = (TemplatesResource) ResourceSystem.getInstance().getResource(RES_FILE);

    private Templates() {
    }

    public static final String AUTH_TPL = TR.getAUTH_TPL();
    public static final String REGISTER_TPL = TR.getREGISTER_TPL();
    public static final String MAIN_TPL = TR.getMAIN_TPL();
    public static final String TIMER_TPL = TR.getTIMER_TPL();
    public static final String USER_TPL = TR.getUSER_TPL();
}
