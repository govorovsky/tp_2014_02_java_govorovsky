package db;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Andrew Govorovsky on 03.03.14
 */
public class UsersDAO {
    private final Database db;
    private static final String getUserQuery = "SELECT * FROM users WHERE name = ?";
    private static final String addUserQuery = "INSERT INTO users(name, password) VALUES (?,?) ";
    private static final String deleteUserQuery = "DELETE FROM users WHERE name = ?";

    public UsersDAO(Database db) {
        this.db = db;
    }

    public UserDataSet getUser(String username) throws SQLException {
        return Executor.executeQuery(db.getConnection(), new ExecHandler<UserDataSet>() {
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
        return Executor.executeUpdate(db.getConnection(), addUserQuery, user.getUsername(), user.getPassword()) > 0;
    }

    public boolean deleteUser(String username) throws SQLException {
        return Executor.executeUpdate(db.getConnection(), deleteUserQuery, username) > 0;
    }
}
