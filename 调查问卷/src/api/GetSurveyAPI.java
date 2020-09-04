package api;

import com.alibaba.fastjson.JSON;
import model.Survey;
import service.SurveyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/api/get-survey.json")
public class GetSurveyAPI extends AbsApiHttpServlet {
    private SurveyService surveyService;

    @Override
    public void init() throws ServletException {
        surveyService = new SurveyService();
    }

    @Override
    public JSON getData(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String sidStr = req.getParameter("sid");
        if (sidStr == null) {
            throw new RuntimeException("sid 不能为空");
        }

        int sid = Integer.parseInt(sidStr);
        Survey survey = surveyService.get(sid);
        System.out.println(survey);

        return Survey.jsonUtil.toJSON(survey);
    }
}
