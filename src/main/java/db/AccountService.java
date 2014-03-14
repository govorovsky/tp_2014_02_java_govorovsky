package db;

import com.sun.istack.internal.NotNull;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Andrew Govorovsky on 14.03.14
 */
public interface AccountService {
    void register(@NotNull HttpServletRequest request) throws AccountServiceException, EmptyDataException;

    void delete(@NotNull String username) throws AccountServiceException;

    void authenticate(@NotNull HttpServletRequest request) throws AccountServiceException, EmptyDataException;

    void logout(@NotNull HttpSession session);

}
