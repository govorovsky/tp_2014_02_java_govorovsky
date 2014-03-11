package db;

import com.sun.istack.internal.NotNull;
import exceptions.AccountServiceException;
import exceptions.EmptyDataException;

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
            if (dao.getUser(username) != null) throw new AccountServiceException("try another name");
            if (!dao.saveUser(new UserDataSet(username, password))) throw new AccountServiceException("try again");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException("db error!");
        }
    }

    public void delete(@NotNull String username) throws AccountServiceException {
        try {
            if (!dao.deleteUser(username)) throw new AccountServiceException("no such user");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException("db error!");
        }
    }


    public void authenticate(@NotNull String username, @NotNull String password) throws AccountServiceException {
        try {
            UserDataSet user = dao.getUser(username);
            if (user == null) throw new AccountServiceException("User not found");
            if (!user.getPassword().equals(password)) throw new AccountServiceException("Wrong password");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException("db error!");
        }
    }


    public void checkLoginPassword(String login, String pass) throws EmptyDataException {
        if (login == null || pass == null || "".equals(login) || "".equals(pass))
            throw new EmptyDataException("Enter login and pass!");
    }
}
