package model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class Question {
    public String question;
    public String type;
    public List<String> optionList;

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", type='" + type + '\'' +
                ", optionList=" + optionList +
                '}';
    }

    public static class JSONUtil implements JSONSerializable<Question> {
        @Override
        public JSONObject toJSON(Question model) {
            JSONObject object = new JSONObject();

            if (model.question != null) {
                object.put("question", model.question);
            }
            if (model.type != null) {
                object.put("type", model.type);
            }
            if (model.optionList != null) {
                JSONArray array = new JSONArray();
                array.addAll(model.optionList);
                object.put("options", array);
            }

            return object;
        }

        @Override
        public Question fromJSON(JSONObject object) {
            Question question = new Question();

            if (object.containsKey("question")) {
                question.question = object.getString("question");
            }
            if (object.containsKey("type")) {
                question.type = object.getString("type");
            }
            if (object.containsKey("options")) {
                question.optionList = object.getJSONArray("options").toJavaList(String.class);
            }

            return question;
        }
    }

    public static final JSONUtil jsonUtil = new JSONUtil();
}
