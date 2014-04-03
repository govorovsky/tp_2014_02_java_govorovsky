package db;

import java.sql.ResultSet;
import java.sql.SQLException;

import static db.DatabaseConnector.getConnection;

/**
 * Created by Andrew Govorovsky on 03.03.14
 */
public class UsersDAO {
    private static final String getUserQuery = "SELECT * FROM users WHERE name = ?";
    private static final String addUserQuery = "INSERT INTO users(name, password) VALUES (?,?) ";
    private static final String deleteUserQuery = "DELETE FROM users WHERE name = ?";

    public UsersDAO() {
    }

    public UserDataSet getUser(String username) throws SQLException {
        return Executor.executeQuery(getConnection(), new ExecHandler<UserDataSet>() {
            @Override
            public UserDataSet handle(ResultSet res) throws SQLException {
                if (res.next()) {
                    return new UserDataSet(res.getInt("id"), res.getString("name"), res.getString("password"));
                } else {
                    return null;
                }
            }
        }, getUserQuery, username);
    }

    public boolean saveUser(UserDataSet user) throws SQLException {
        return Executor.executeUpdate(getConnection(), addUserQuery, user.getUsername(), user.getPassword()) > 0;
    }

    public boolean deleteUser(String username) throws SQLException {
        return Executor.executeUpdate(getConnection(), deleteUserQuery, username) > 0;
    }
}
