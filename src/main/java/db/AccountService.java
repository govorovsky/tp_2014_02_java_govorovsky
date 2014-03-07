package db;

import exceptions.AccountServiceException;
import exceptions.EmptyDataException;

import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 22.02.14
 */
public class AccountService {
    private UsersDAO dao;

    public AccountService() {
        dao = new UsersDAO(DatabaseConnector.getConnection());
    }

    public void register(String username, String password) throws AccountServiceException {
        try {
            if (dao.getUser(username) != null) throw new AccountServiceException("user not found");
            dao.saveUser(new UserDataSet(username, password));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AccountServiceException("db error!");
        }
    }

    public void authenticate(String username, String password) throws AccountServiceException {
        try {
            UserDataSet user = dao.getUser(username);
            if(user == null) throw new AccountServiceException("User not found");
            if(!user.getPassword().equals(password)) throw  new AccountServiceException("Wrong password");
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
