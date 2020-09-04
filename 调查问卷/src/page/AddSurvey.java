package page;

import service.SurveyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/add-survey")
public class AddSurvey extends HttpServlet {
    private SurveyService surveyService;

    @Override
    public void init() throws ServletException {
        surveyService = new SurveyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int sid = surveyService.insert();
            resp.sendRedirect("/editor.jsp?sid=" + sid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
