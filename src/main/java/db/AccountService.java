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
public class AccountService {
    private UsersDAO dao;

    public AccountService() {
        dao = new UsersDAO(DatabaseConnector.getConnection());
    }

    public AccountService(Connection conn) {
        dao = new UsersDAO(conn);
    }

    public void register(@NotNull String username, @NotNull String password) throws AccountServiceException {
        try {
            if (dao.getUser(username) != null) throw new AccountServiceException(ExceptionMessages.USER_ALREADY_EXISTS);
            if (!dao.saveUser(new UserDataSet(username, password))) throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }

    public void delete(@NotNull String username) throws AccountServiceException {
        try {
            if (!dao.deleteUser(username)) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }


    public void authenticate(@NotNull String username, @NotNull String password) throws AccountServiceException {
        try {
            UserDataSet user = dao.getUser(username);
            if (user == null) throw new AccountServiceException(ExceptionMessages.NO_SUCH_USER_FOUND);
            if (!user.getPassword().equals(password)) throw new AccountServiceException(ExceptionMessages.FAILED_AUTH);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException(ExceptionMessages.SQL_ERROR);
        }
    }


    public void checkLoginPassword(String login, String pass) throws EmptyDataException {
        if (login == null || pass == null || "".equals(login) || "".equals(pass))
            throw new EmptyDataException(ExceptionMessages.EMPTY_DATA);
    }
}
