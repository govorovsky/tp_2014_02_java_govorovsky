package db;

import java.sql.Connection;

/**
 * Created by Andrew Govorovsky on 06.04.14
 */
public abstract class Database {

    protected Connection connection;

    abstract public Connection getConnection();

}
