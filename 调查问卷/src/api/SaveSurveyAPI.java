package api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import model.Survey;
import service.SurveyService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/save-survey.json")
public class SaveSurveyAPI extends AbsApiHttpServlet {
    private SurveyService surveyService;

    @Override
    public void init() throws ServletException {
        surveyService = new SurveyService();
    }

    @Override
    public JSON postData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        JSONObject json = getJSON(req);
        Survey survey = Survey.jsonUtil.fromJSON(json);
        System.out.println(survey);
        surveyService.save(survey);
        return Survey.jsonUtil.toJSON(survey);
    }
}
