package api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import model.Survey;
import service.SurveyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/list-surveys.json")
public class ListSurveysAPI extends AbsApiHttpServlet {
    private SurveyService surveyService;

    @Override
    public void init() throws ServletException {
        surveyService = new SurveyService();
    }

    @Override
    public JSON getData(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        List<Survey> surveyList = surveyService.list();

        return Survey.jsonUtil.toJSON(surveyList);
    }
}
