package db;

import java.sql.Connection;

/**
 * Created by Andrew Govorovsky on 06.04.14
 */
interface Database {
    Connection getConnection();
}
