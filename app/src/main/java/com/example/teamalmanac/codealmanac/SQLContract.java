package com.example.teamalmanac.codealmanac;

import android.provider.BaseColumns;

/**
 * Created by Choi Jaeung on 2016-11-09.
 */

//DB 정의서
public final class SQLContract {
    public static final String DATABASE_NAME = "AlmanacDB.db";

    //사용자 이름 테이블
    public static abstract class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user_table";
        public static final String COLUMN_NAME_UUID = "user_uuid";
        public static final String COLUMN_NAME_NAME = "user_name";
    }

    // 할일 테이블
    public static abstract class ToDoEntry implements BaseColumns{
        public static final String TABLE_NAME = "todo_table";
        public static final String COLUMN_NAME_TODO = "todo_text";
        public static final String COLUMN_NAME_DATE = "todo_date";

        public static final String COLUMN_NAME_CHECK = "todo_check";
    }

    public static abstract class MainFocusEntry implements  BaseColumns{
        public static final String TABLE_NAME = "main_focus_table";
        public static final String COLUMN_NAME_DATE = "main_focus_date";
        public static final String COLUMN_NAME_MAIN_FOCUS = "main_focus_text";

        public static final String COLUMN_NAME_CHECK = "main_focus_check";
    }
}
