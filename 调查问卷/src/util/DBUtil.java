package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static volatile DataSource dataSource = null;

    private static DataSource initDataSource() {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/survey?characterEncoding=utf-8&useSSL=false");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("");

        return mysqlDataSource;
    }

    // 单例的懒汉模式 - double check
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            synchronized (DBUtil.class) {
                if (dataSource == null) {
                    dataSource = initDataSource();
                }
            }
        }

        return dataSource.getConnection();
    }



    public static <T> T selectOne(String sql, String[] values, ResultSetToModel<T> toModel) throws SQLException {
        try (Connection c = getConnection()) {
            try (PreparedStatement s = c.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    s.setString(i + 1, values[i]);
                }

                try (ResultSet rs = s.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    return toModel.apply(rs);
                }
            }
        }
    }

    public static <T> List<T> selectMore(String sql, String[] values, ResultSetToModel<T> toModel) throws SQLException {
        List<T> list = new ArrayList<>();

        try (Connection c = getConnection()) {
            try (PreparedStatement s = c.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    s.setString(i + 1, values[i]);
                }

                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        list.add(toModel.apply(rs));
                    }
                }
            }
        }

        return list;
    }

    public static void updateOrInsertOrDelete(String sql, String[] values) throws SQLException {
        try (Connection c = getConnection()) {
            try (PreparedStatement s = c.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    s.setString(i + 1, values[i]);
                }

                s.executeUpdate();
            }
        }
    }
}
