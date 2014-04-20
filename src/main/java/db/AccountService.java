package db;

import messageSystem.Abonent;
import util.Result;
import util.UserState;


/**
 * Created by Andrew Govorovsky on 14.03.14
 */
public interface AccountService extends Abonent, Runnable {

    UserState register(String username, String password);

    UserState delete(String username);

    Result<Long> authenticate(String username, String password);

}
