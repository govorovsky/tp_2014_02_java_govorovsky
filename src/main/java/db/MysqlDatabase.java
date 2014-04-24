package db;

import resource.DBResource;
import resource.ResourceSystem;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * Created by Andrew Govorovsky on 06.04.14
 */
public class MysqlDatabase implements Database {

    private static final String MYSQL_RES = "mysql.xml";

    private final String host;
    private final int port;
    private final String db;
    private final String user;
    private final String pass;
    private final String driver;

    private Connection connection;

    public MysqlDatabase() {
        DBResource dbResource = (DBResource) ResourceSystem.getInstance().getResource(MYSQL_RES);
        host = dbResource.getHost();
        port = dbResource.getPort();
        db = dbResource.getDb();
        user = dbResource.getUser();
        pass = dbResource.getPass();
        driver = dbResource.getDriver();
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            } else {
                DriverManager.registerDriver((Driver) Class.forName(driver).newInstance());
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
