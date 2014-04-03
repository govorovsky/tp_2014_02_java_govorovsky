package messageSystem;

import frontend.Frontend;
import frontend.UserSession;

/**
 * Created by Andrew Govorovsky on 31.03.14
 */
public class MsgUpdateUserSession extends MsgToFrontend {
    private final UserSession session;

    public MsgUpdateUserSession(Address from, Address to, UserSession session) {
        super(from, to);
        this.session = session;
    }

    void exec(Frontend frontend) {
        frontend.updateUserSession(session);
    }
}
