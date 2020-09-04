package oldfashion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListSurvey extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 从数据库读取数据
        // 2. 把数据模型转化为 JSON 格式
        // 3. 把最终效果使用标准 API 的形式返回

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/survey?characterEncoding=utf-8&useSSL=false");
        mysqlDataSource.setUser("root");
        DataSource dataSource = mysqlDataSource;

        try (Connection c = dataSource.getConnection()) {
            String sql = "select sid sid, u.uid, title, brief, cover_url, nickname from surveys s, users u where s.uid = u.uid and status = '已发布' order by sid desc";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                try (ResultSet r = s.executeQuery()) {
                    JSONArray array = new JSONArray();
                    while (r.next()) {
                        JSONObject object = new JSONObject();
                        object.put("sid", r.getInt("sid"));
                        object.put("title", r.getString("title"));
                        object.put("title", r.getString("title"));
                        object.put("title", r.getString("title"));

                        JSONObject user = new JSONObject();
                        user.put("uid", r.getInt("uid"));
                        user.put("nickname", r.getString("nickname"));
                        object.put("user", user);

                        array.add(object);
                    }

                    JSONObject result = new JSONObject();
                    result.put("status", 200);
                    result.put("data", array);

                    resp.setContentType("application/json; charset=utf-8");
                    resp.getWriter().println(result.toJSONString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject object = new JSONObject();
            object.put("status", 500);
            object.put("reason", e.getMessage());

            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().println(object.toJSONString());
        }
    }
}
