package db;

import com.sun.istack.internal.NotNull;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import messageSystem.Abonent;
import messageSystem.MessageSystem;


/**
 * Created by Andrew Govorovsky on 14.03.14
 */
public interface AccountService extends Abonent, Runnable {

    void register(String username, String password) throws AccountServiceException, EmptyDataException;

    void delete(@NotNull String username) throws AccountServiceException;

    long authenticate(String username, String password) throws AccountServiceException, EmptyDataException;

    MessageSystem getMessageSystem();
}
