package com.example.shq.subjecttimetable.other;

import android.provider.BaseColumns;

/**
 * Created by shq on 2018/7/8.
 */

public final class CourseTable {
    private CourseTable(){}
    public static class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME="courseTable";
        public static final String COLUMN_NAME_USER="username";
        public static final String COLUMN_NAME_NAME="name";
        public static final String COLUMN_NAME_NUMBER="couNumber";
        public static final String COLUMN_NAME_ROOM="room";
        public static final String COLUMN_NAME_TEACHER="teacher";
        public static final String COLUMN_NAME_WEEKS="weeklist";
        public static final String COLUMN_NAME_SATRT="start";
        public static final String COLUMN_NAME_STEP="step";
        public static final String COLUMN_NAME_DAY="day";
    }
}
