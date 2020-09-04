package util;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetToModel<T> {
    T apply(ResultSet resultSet) throws SQLException;
}
