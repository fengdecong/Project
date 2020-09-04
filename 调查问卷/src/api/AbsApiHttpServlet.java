package api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

public abstract class AbsApiHttpServlet extends HttpServlet {
    public static final JSONObject empty = new JSONObject();

    public JSON getData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        return empty;
    }

    public JSON postData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        return empty;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");

        JSONObject result = new JSONObject();
        try {
            JSON object = getData(req, resp);
            if (object == empty) {
                result.put("status", 405);
                result.put("reason", "MethodNotAllowed");
            } else {
                result.put("status", 200);
                result.put("data", object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("reason", e.getMessage());
        }

        resp.getWriter().println(result.toJSONString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");

        JSONObject result = new JSONObject();
        try {
            JSON object = postData(req, resp);
            if (object == empty) {
                result.put("status", 405);
                result.put("reason", "MethodNotAllowed");
            } else {
                result.put("status", 200);
                result.put("data", object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", 500);
            result.put("reason", e.getMessage());
        }

        resp.getWriter().println(result.toJSONString());
    }

    public JSONObject getJSON(HttpServletRequest req) throws IOException {
        // 从请求体中读数据的字符输入流
        req.setCharacterEncoding("utf-8");
        BufferedReader reader = req.getReader();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\r\n");
        }

        return JSON.parseObject(sb.toString());
    }
}
