package db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * Created by Andrew Govorovsky on 06.04.14
 */
public class MysqlDatabase extends Database {

    private static final String host = "localhost";
    private static final int port = 3306;
    private static final String db = "gameserver";
    private static final String user = "root";
    private static final String pass = "qazxsw12";

    public MysqlDatabase() {
    }


    @Override
    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
