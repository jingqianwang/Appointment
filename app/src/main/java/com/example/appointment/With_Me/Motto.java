package com.example.appointment.With_Me;

import org.json.JSONObject;

/**
 * Created by work on 2017/9/12.
 */

public class Motto {

    private String motto;

    private String author;

    public String getMotto() {
        return motto;
    }

    public String getAuthor() {
        return author;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    //解析返回的名人名言和作者
    public boolean parseMotto(String jsonData){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.getJSONObject(jsonData);
            setMotto(jsonObject.getString("famous_name"));
            setAuthor(jsonObject.getString("famous_saying"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
