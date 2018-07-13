package com.example.shq.subjecttimetable.other;

import com.zhuangfei.timetable.core.SubjectBean;

/**
 * Created by shq on 2018/6/12.
 */

public class MySubjectBean extends SubjectBean {

    private int _id;

    private String couNumber;


    public String getCouNumber() {
        return couNumber;
    }

    public void setCouNumber(String couNumber) {
        this.couNumber = couNumber;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
