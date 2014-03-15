package db;

import com.sun.istack.internal.NotNull;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;
import exceptions.ExceptionMessages;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 22.02.14
 */
public class AccountServiceImpl implements AccountService {
    private UsersDAO dao;

    public AccountServiceImpl() {
        dao = new UsersDAO(DatabaseConnector.getConnection());
    }

    public AccountServiceImpl(Connection conn) {
        dao = new UsersDAO(conn);
    }

    @Override
    public void register(String username, String password) throws AccountServiceException, EmptyDataException {
        try {
            checkLoginPassword(username, password);
            if (dao.getUser(username) != null) throw new AccountServiceException(ExceptionMessages.USER_ALREADY_EXISTS);
            if (!dao.saveUser(new UserDataSet(username, password)))
                throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }

    @Override
    public void delete(@NotNull String username) throws AccountServiceException {
        try {
            if (!dao.deleteUser(username)) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }


    @Override
    public long authenticate(String username, String password) throws AccountServiceException, EmptyDataException {
        try {
            checkLoginPassword(username, password);
            UserDataSet user = dao.getUser(username);
            if (user == null) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
            if (!user.getPassword().equals(password)) throw new AccountServiceException(ExceptionMessages.FAILED_AUTH);
            return user.getUserId();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }

    @Override
    public void logout() {
    }


    private void checkLoginPassword(String username, String pass) throws EmptyDataException {
        if (username == null || pass == null || "".equals(username) || "".equals(pass))
            throw new EmptyDataException(ExceptionMessages.EMPTY_DATA);
    }
}
