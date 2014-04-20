package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 06.04.14
 */
public class HsqlDatabase implements Database {

    private Connection connection;

    public HsqlDatabase() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                connection = DriverManager.getConnection("jdbc:hsqldb:file:hsqldb/testdb;shutdown=true", "SA", "");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
