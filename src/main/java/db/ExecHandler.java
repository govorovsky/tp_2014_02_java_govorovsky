package db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 03.03.14 {15:02}
 */
public interface ExecHandler<T> {
    public T handle(ResultSet res) throws SQLException;
}
