package db;

import resource.DBResource;
import resource.ResourceSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 06.04.14
 */
public class HsqlDatabase implements Database {

    private static final String HSQL_RES = "hsqldb.xml";
    private final String user;
    private final String pass;
    private final String db;

    private Connection connection;

    public HsqlDatabase() {
        DBResource dbResource = (DBResource) ResourceSystem.getInstance().getResource(HSQL_RES);
        user = dbResource.getUser();
        pass = dbResource.getPass();
        db = dbResource.getDb();

        try {
            Class.forName(dbResource.getDriver());
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
                connection = DriverManager.getConnection("jdbc:hsqldb:file:" + db + ";shutdown=true", user, pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
