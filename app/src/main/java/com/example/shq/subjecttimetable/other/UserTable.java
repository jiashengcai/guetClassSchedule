package com.example.shq.subjecttimetable.other;

import android.provider.BaseColumns;

/**
 * Created by shq on 2018/7/8.
 */

public final class UserTable {
    private UserTable(){}
    public static class UserEntry implements BaseColumns{
        public static final String TABLE_NAME="userTable";
        public static final String COLUMN_NAME_USERNAME="username";
        public static final String COLUMN_NAME_PASSWORD="password";
    }
}
