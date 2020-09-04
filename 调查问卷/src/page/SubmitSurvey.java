package page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet("/submit-survey")
public class SubmitSurvey extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = req.getParameter(name);

            System.out.println(name + "    " + value);
        }

        // TODO 把收集到的数据保存到数据库的 answers 表即可
        // "好么=好$吃了么=吃了$喝了么=没喝"

        resp.setContentType("text/plain; charset=utf-8");
        resp.getWriter().println("提交成功");
    }
}
