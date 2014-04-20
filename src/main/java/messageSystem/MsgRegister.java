package messageSystem;

import db.AccountService;
import frontend.UserSession;
import util.UserState;

/**
 * Created by Andrew Govorovsky on 02.04.14
 */


public class MsgRegister extends MsgToAccountService {
    private final String username;
    private final String pass;
    private final String ssid;

    public MsgRegister(Address from, Address to, String username, String pass, String ssid) {
        super(from, to);
        this.username = username;
        this.pass = pass;
        this.ssid = ssid;
    }

    @Override
    void exec(AccountService as) {
        UserState result = as.register(username, pass);
        UserSession session = new UserSession(username, ssid, result);
        as.getMessageSystem().sendMessage(new MsgUpdateUserSession(getTo(), getFrom(), session));
    }
}
