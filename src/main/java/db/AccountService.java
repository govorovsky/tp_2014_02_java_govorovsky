package db;

import com.sun.istack.internal.NotNull;
import messageSystem.Abonent;
import util.Result;


/**
 * Created by Andrew Govorovsky on 14.03.14
 */
public interface AccountService extends Abonent, Runnable {

    Result<Boolean> register(String username, String password);

    Result<Boolean> delete(@NotNull String username);

    Result<Long> authenticate(String username, String password);

}
