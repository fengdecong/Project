package model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;


public class Survey {
    public Integer sid;
    public User user;
    public String title;
    public String brief;
    public String coverUrl;
    public String thanks;
    public List<Page> pageList;

    @Override
    public String toString() {
        return "Survey{" +
                "sid=" + sid +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", thanks='" + thanks + '\'' +
                ", pageList=" + pageList +
                '}';
    }

    // 把 JSON 的序列化和反序列代码全部留到这里，方便以后进行修改，或者发现错误
    public static class JSONUtil implements JSONSerializable<Survey> {
        @Override
        public JSONObject toJSON(Survey model) {
            JSONObject object = new JSONObject();
            if (model.sid != null) {
                object.put("sid", model.sid);
            }
            if (model.title != null) {
                object.put("title", model.title);
            }
            if (model.brief != null) {
                object.put("brief", model.brief);
            }
            if (model.coverUrl != null) {
                object.put("coverUrl", model.coverUrl);
            }
            if (model.user != null) {
                object.put("user", User.jsonUtil.toJSON(model.user));
            }
            if (model.thanks != null) {
                object.put("thanks", model.thanks);
            }
            if (model.pageList != null) {
                object.put("pages", Page.jsonUtil.toJSON(model.pageList));
            }

            return object;
        }

        @Override
        public Survey fromJSON(JSONObject object) {
            Survey model = new Survey();

            if (object.containsKey("sid")) {
                model.sid = object.getInteger("sid");
            }
            if (object.containsKey("user")) {
                model.user = User.jsonUtil.fromJSON(object.getJSONObject("user"));
            }
            if (object.containsKey("title")) {
                model.title = object.getString("title");
            }
            if (object.containsKey("brief")) {
                model.brief = object.getString("brief");
            }
            if (object.containsKey("coverUrl")) {
                model.coverUrl = object.getString("coverUrl");
            }
            if (object.containsKey("thanks")) {
                model.thanks = object.getString("thanks");
            }
            if (object.containsKey("pages")) {
                model.pageList = Page.jsonUtil.fromJSON(object.getJSONArray("pages"));
            }

            return model;
        }
    }
    public static final JSONUtil jsonUtil = new JSONUtil();
}
