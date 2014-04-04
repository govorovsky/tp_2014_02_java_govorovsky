package messageSystem;

import db.AccountService;
import db.AccountServiceMessages;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import frontend.UserSession;

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
        Long id;
        UserSession session;
        try {
            id = as.authenticate(username, pass);
            session = new UserSession(username, ssid, AccountServiceMessages.AUTHORIZED, id);
        } catch (EmptyDataException | AccountServiceException e) {
            session = new UserSession(username, ssid, e.getMessage());
        }
        as.getMessageSystem().sendMessage(new MsgUpdateUserSession(getTo(), getFrom(), session));
    }
}
