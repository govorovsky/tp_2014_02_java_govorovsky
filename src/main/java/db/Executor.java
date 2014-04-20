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
        PreparedStatement stm = connection.prepareStatement(query);
        try {
            for (int i = 0; i < vars.length; i++) {
                stm.setObject(i + 1, vars[i]);
            }
            ResultSet resultSet = stm.executeQuery();
            try {
                return handler.handle(resultSet);
            } finally {
                if (resultSet != null) resultSet.close();
            }
        } finally {
            if (stm != null) stm.close();
        }
    }

    public static int executeUpdate(Connection connection, String query, Object... vars) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(query);
        try {
            for (int i = 0; i < vars.length; i++) {
                stm.setObject(i + 1, vars[i]);
            }
            return stm.executeUpdate();
        } finally {
            if (stm != null) stm.close();
        }
    }
}
