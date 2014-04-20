package messageSystem;

import db.AccountService;
import frontend.UserSession;
import util.Result;

/**
 * Created by Andrew Govorovsky on 01.04.14
 */
public class MsgLogin extends MsgToAccountService {
    private final String username;
    private final String pass;
    private final String ssid;

    public MsgLogin(Address from, Address to, String username, String pass, String ssid) {
        super(from, to);
        this.username = username;
        this.pass = pass;
        this.ssid = ssid;
    }

    @Override
    void exec(AccountService as) {
        Result<Long> result = as.authenticate(username, pass);
        UserSession session = new UserSession(username, ssid, result.getStatus(), (result.getStatus().isSuccess()) ? result.getData() : null);
        as.getMessageSystem().sendMessage(new MsgUpdateUserSession(getTo(), getFrom(), session));
    }
}
