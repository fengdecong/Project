package model;

import com.alibaba.fastjson.JSONObject;

public class User {
    public Integer uid;
    public String username;
    public String nickname;

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public static class JSONUtil implements JSONSerializable<User> {
        @Override
        public JSONObject toJSON(User model) {
            JSONObject object = new JSONObject();
            if (model.uid != null) {
                object.put("uid", model.uid);
            }
            if (model.username != null) {
                object.put("username", model.username);
            }
            if (model.nickname != null) {
                object.put("nickname", model.nickname);
            }
            return object;
        }

        @Override
        public User fromJSON(JSONObject object) {
            User model = new User();

            if (object.containsKey("uid")) {
                model.uid = object.getInteger("uid");
            }
            if (object.containsKey("nickname")) {
                model.nickname = object.getString("nickname");
            }
            if (object.containsKey("username")) {
                model.username = object.getString("username");
            }

            return model;
        }
    }
    public static final JSONUtil jsonUtil = new JSONUtil();
}
