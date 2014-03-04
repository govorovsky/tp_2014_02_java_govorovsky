package db;

import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 22.02.14
 */
public class AccountService {
    private UsersDAO dao;

    public AccountService() {
        dao = new UsersDAO(DatabaseConnector.getConnection());
    }

    public boolean addUser(String username, String password) {
        try {
            return (dao.getUser(username) == null && dao.saveUser(new UserDataSet(username, password)));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticate(String username, String password) {
        try {
            UserDataSet user = dao.getUser(username);
            return (user != null && user.getPassword().equals(password));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
