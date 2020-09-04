package model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class Page {
    public List<Question> questionList;

    @Override
    public String toString() {
        return "Page{" +
                "questionList=" + questionList +
                '}';
    }

    public static class JSONUtil implements JSONSerializable<Page> {
        @Override
        public JSONObject toJSON(Page model) {
            JSONObject object = new JSONObject();

            if (model.questionList != null) {
                object.put("questions", Question.jsonUtil.toJSON(model.questionList));
            }

            return object;
        }

        @Override
        public Page fromJSON(JSONObject object) {
            Page page = new Page();

            if (object.containsKey("questions")) {
                page.questionList = Question.jsonUtil.fromJSON(object.getJSONArray("questions"));
            }

            return page;
        }
    }
    public static final JSONUtil jsonUtil = new JSONUtil();
}
