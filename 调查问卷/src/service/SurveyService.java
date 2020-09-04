package service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import model.Page;
import model.Question;
import model.Survey;
import model.User;
import util.DBUtil;
import util.ResultSetToModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class SurveyService {
    public List<Survey> list() throws SQLException {
        String[] values = {};
        return DBUtil.selectMore(
                "select sid sid, u.uid, title, brief, cover_url, nickname from surveys s, users u where s.uid = u.uid and status = '已发布' order by sid desc",
                values,
                r -> {
                    Survey survey = new Survey();
                    survey.sid = r.getInt("sid");
                    survey.title = r.getString("title");
                    survey.brief = r.getString("brief");
                    survey.coverUrl = r.getString("cover_url");

                    User user = new User();
                    user.uid = r.getInt("uid");
                    user.nickname = r.getString("nickname");

                    survey.user = user;

                    return survey;
                });
    }

    public Survey get(int sid) throws SQLException {
        final Survey survey = new Survey();

        // 基本信息 + 致谢词从 surveys 表中查询
        String[] values = new String[]{ String.valueOf(sid) };
        DBUtil.selectOne(
                "SELECT sid, title, brief, cover_url, thanks FROM surveys where sid = ?",
                values,
                resultSet -> {
                    survey.sid = sid;
                    survey.title = resultSet.getString("title");
                    survey.brief = resultSet.getString("brief");
                    survey.coverUrl = resultSet.getString("cover_url");
                    survey.thanks = resultSet.getString("thanks");

                    return survey;
                });

        LinkedHashMap<Integer, List<Question>> map = new LinkedHashMap<>();
        DBUtil.selectMore(
                "SELECT question, type, options, pid FROM questions where sid = ? ORDER BY pid, qid",
                values,
                resultSet -> {
                    int pid = resultSet.getInt("pid");

                    Question question = new Question();
                    question.question = resultSet.getString("question");
                    question.type = resultSet.getString("type");
                    String[] options = resultSet.getString("options").split("\\$");
                    question.optionList = Arrays.asList(options);

                    List<Question> list = map.computeIfAbsent(pid, k -> new ArrayList<>());

                    list.add(question);

                    return null;
                });

        survey.pageList = new ArrayList<>();
        // 严格按照插入顺序
        for (Map.Entry<Integer, List<Question>> entry : map.entrySet()) {
            Page page = new Page();
            page.questionList = entry.getValue();
            survey.pageList.add(page);
        }

        return survey;
    }

    public void save(Survey survey) throws SQLException {
        // 投机取巧的方法(性能不是很高，但逻辑处理比较简单）:
        // 1. 保存基本信息 UPDATE
        // 2. 保存问题:
        //    1. 找到之前的问题就 UPDATE
        //    2. 如果有新问题，就INSERT
        //    3. 如果有问题删除就 DELETE
        //
        //    1. 删除 sid 下的所有的问题
        //    2. 把所有问题都当成新问题来保存

        String[] values1 = { survey.title, survey.brief, survey.coverUrl, survey.thanks, String.valueOf(survey.sid)};
        DBUtil.updateOrInsertOrDelete("UPDATE surveys SET title = ?, brief = ?, cover_url = ?, thanks = ? WHERE sid = ?", values1);

        DBUtil.updateOrInsertOrDelete("DELETE FROM questions WHERE sid = ?", new String[] { String.valueOf(survey.sid )});

        int pid = 0;
        for (Page page : survey.pageList) {
            pid++;
            for (Question question : page.questionList) {
                String options = "";
                if (question.optionList != null) {
                    options = String.join("$", question.optionList);
                }
                String[] values = {String.valueOf(survey.sid), String.valueOf(pid), question.question, question.type, options};
                DBUtil.updateOrInsertOrDelete("INSERT INTO questions (sid, pid, question, type, options) VALUES (?, ?, ?, ?, ?)", values);
            }
        }
    }

    public int insert() throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            String sql = "INSERT INTO surveys (uid, title, brief, cover_url, thanks, status) VALUES (1, '请输入调查问卷题目', '请给与简单介绍', '/img/cover.png', '感谢参与', '已发布')";
            try (PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                s.executeUpdate();

                try (ResultSet r = s.getGeneratedKeys()) {
                    r.next();

                    return r.getInt(1);
                }
            }
        }
    }
}
