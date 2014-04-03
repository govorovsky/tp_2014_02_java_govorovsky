package db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 03.03.14
 */
public class DatabaseConnector {

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            connection = DriverManager.getConnection("jdbc:mysql://" + "localhost:" + "3306/" + "gameserver?" + "user=root&" + "password=qazxsw12");
            return connection;
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

