package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrew Govorovsky on 03.03.14 {14:58}
 */
public class Executor {
    public static <T> T executeQuery(Connection connection, ExecHandler<T> handler, String query, Object... vars) throws SQLException {
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            for (int i = 0; i < vars.length; i++) {
                stm.setObject(i + 1, vars[i]);
            }
            try (ResultSet resultSet = stm.executeQuery()) {
                return handler.handle(resultSet);
            }
        }
    }

    public static int executeUpdate(Connection connection, String query, Object... vars) throws SQLException {
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            for (int i = 0; i < vars.length; i++) {
                stm.setObject(i + 1, vars[i]);
            }
            return stm.executeUpdate();
        }
    }
}
