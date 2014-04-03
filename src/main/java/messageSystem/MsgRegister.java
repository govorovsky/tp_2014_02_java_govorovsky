package messageSystem;

import db.AccountService;
import db.AccountServiceMessages;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import frontend.UserSession;

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

    void exec(AccountService as) {
        UserSession session = new UserSession(username, ssid);
        try {
            as.register(username, pass);
            session.setStatus(AccountServiceMessages.USER_ADDED);
        } catch (EmptyDataException | AccountServiceException e) {
            session.setStatus(e.getMessage());
        }
        as.getMessageSystem().sendMessage(new MsgUpdateUserSession(getTo(), getFrom(), session));
    }
}
