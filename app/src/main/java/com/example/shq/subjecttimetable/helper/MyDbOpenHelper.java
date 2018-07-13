package com.example.shq.subjecttimetable.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shq.subjecttimetable.other.CourseTable;
import com.example.shq.subjecttimetable.other.MySubjectBean;
import com.example.shq.subjecttimetable.other.UserTable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shq on 2018/7/8.
 */

public class MyDbOpenHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_COURSETABLE="CREATE TABLE "+ CourseTable.CourseEntry.TABLE_NAME+
            "("+CourseTable.CourseEntry._ID+" INTEGER PRIMARY KEY, "+CourseTable.CourseEntry.COLUMN_NAME_USER
            +" VARCHAR(40), "+CourseTable.CourseEntry.COLUMN_NAME_NAME+" VARCHAR(80), "+
            CourseTable.CourseEntry.COLUMN_NAME_NUMBER+" VARCHAR(20), "+
            CourseTable.CourseEntry.COLUMN_NAME_ROOM+" VARCHAR(40), "+
            CourseTable.CourseEntry.COLUMN_NAME_TEACHER+" VARCHAR(40), "+
            CourseTable.CourseEntry.COLUMN_NAME_WEEKS+" VARCHAR(40), "+
            CourseTable.CourseEntry.COLUMN_NAME_SATRT+" INTEGER ,"+
            CourseTable.CourseEntry.COLUMN_NAME_STEP+" INTEGER ,"+
            CourseTable.CourseEntry.COLUMN_NAME_DAY+" INTEGER"+")";
    private static final String SQL_DELETE_COURSETABLE= "DROP TABLE IF EXISTS " + CourseTable.CourseEntry.TABLE_NAME;

    private static final String SQL_CREATE_USERTABLE="CREATE TABLE "+ UserTable.UserEntry.TABLE_NAME+
            "("+ UserTable.UserEntry.COLUMN_NAME_USERNAME +" VARCHAR(40), "
            +UserTable.UserEntry.COLUMN_NAME_PASSWORD+" VARCHAR(80))";
    private static final String SQL_DELETE_USERTABLE= "DROP TABLE IF EXISTS " + UserTable.UserEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydb.db";
    public MyDbOpenHelper(Context context) {
        super(context , DATABASE_NAME, null , DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(SQL_CREATE_COURSETABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERTABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase , int i , int i1){
        sqLiteDatabase.execSQL(SQL_CREATE_COURSETABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERTABLE);
    }
    public void addUser(String username,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO userTable values(?,?)",
                new String[]{username,password});
    }
    public String getUser(String username,String password){//从数据库查出用户名
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM userTable WHERE username = ? and password= ?",new String[]{username,password});
        cursor.moveToFirst();
        if(cursor.getCount()==0)
            return "";
        return cursor.getString(cursor.getColumnIndex("username"));
    }

    public void deleteUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM person WHERE username = ?",
                new String[]{username});
    }
    public void addCourseTable(List<MySubjectBean> mySubjectBeanList,String username){
        SQLiteDatabase db = this.getWritableDatabase();
        for (MySubjectBean msb:mySubjectBeanList
             ) {
            String weeklistStr="";
            for(int i=0;i<msb.getWeekList().size();i++){
                weeklistStr+=msb.getWeekList().get(i)+",";
            }
            db.execSQL("INSERT INTO courseTable values(NULL,?,?,?,?,?,?,?,?,?)",
                    new String[]{username,msb.getName(),msb.getCouNumber(),msb.getRoom(),
                    msb.getTeacher(),weeklistStr,
                    String.valueOf(msb.getStart()),String.valueOf(msb.getStep()),
                    String.valueOf(msb.getDay())});
        }
    }

    public void addCourseTable(MySubjectBean mySubjectBeanList,String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String weeklistStr="";
        for(int i=0;i<mySubjectBeanList.getWeekList().size();i++){
            weeklistStr+=mySubjectBeanList.getWeekList().get(i)+",";
        }
        db.execSQL("INSERT INTO courseTable values(NULL,?,?,?,?,?,?,?,?,?)",
                new String[]{username,mySubjectBeanList.getName(),mySubjectBeanList.getCouNumber(),mySubjectBeanList.getRoom(),
                        mySubjectBeanList.getTeacher(),weeklistStr,
                        String.valueOf(mySubjectBeanList.getStart()),String.valueOf(mySubjectBeanList.getStep()),
                        String.valueOf(mySubjectBeanList.getDay())});
        }

    public void deleteCourseTableByUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM courseTable WHERE username = ?",
                new String[]{username});
    }

    public void deleteCourseTableById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM courseTable WHERE _id = ?",
                new String[]{String.valueOf(id)});
    }

    public List<MySubjectBean> getCourseTableByUsername(String username){
        List<MySubjectBean> mySubjectBeanList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM courseTable WHERE username = ?",new String[]{username});
       // Cursor cursor = db.query("courseTable", new String[] {"*"}, "username = ?", new String[] {username}, null, null, null);
        if(cursor.moveToFirst()){
            do{
                //遍历Cursor对象，取出数据
                MySubjectBean mySubjectBean=new MySubjectBean();

                mySubjectBean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
                mySubjectBean.setName(cursor.getString(cursor.getColumnIndex("name")));
                mySubjectBean.setCouNumber(cursor.getString(cursor.getColumnIndex("couNumber")));
                mySubjectBean.setRoom(cursor.getString(cursor.getColumnIndex("room")));
                mySubjectBean.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
                String weeklistStr=cursor.getString(cursor.getColumnIndex("weeklist"));
                String[] wLS=weeklistStr.split(",");
                List<Integer> weekList=new ArrayList<>();
                for(int i=0;i<wLS.length;i++){
                    weekList.add(Integer.parseInt(wLS[i]));
                }
                mySubjectBean.setWeekList(weekList);
                mySubjectBean.setStart(cursor.getInt(cursor.getColumnIndex("start")));
                mySubjectBean.setStep(cursor.getInt(cursor.getColumnIndex("step")));
                mySubjectBean.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                mySubjectBeanList.add(mySubjectBean);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return mySubjectBeanList;
    }

    public MySubjectBean getCourseById(int id){
        MySubjectBean mySubjectBean=new MySubjectBean();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM courseTable WHERE _id = ?",new String[]{String.valueOf(id)});
        if(cursor.getCount()==0)
            return null;
        cursor.moveToLast();
        mySubjectBean.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
        mySubjectBean.setName(cursor.getString(cursor.getColumnIndex("name")));
        mySubjectBean.setCouNumber(cursor.getString(cursor.getColumnIndex("couNumber")));
        mySubjectBean.setRoom(cursor.getString(cursor.getColumnIndex("room")));
        mySubjectBean.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
        String weeklistStr=cursor.getString(cursor.getColumnIndex("weeklist"));
        String[] wLS=weeklistStr.split(",");
        List<Integer> weekList=new ArrayList<>();
        for(int i=0;i<wLS.length;i++){
            weekList.add(Integer.parseInt(wLS[i]));
        }
        mySubjectBean.setWeekList(weekList);
        mySubjectBean.setStart(cursor.getInt(cursor.getColumnIndex("start")));
        mySubjectBean.setStep(cursor.getInt(cursor.getColumnIndex("step")));
        mySubjectBean.setDay(cursor.getInt(cursor.getColumnIndex("day")));
        return mySubjectBean;
    }

    public void updateCourseById(MySubjectBean mySubjectBean){
        String weeklistStr="";
        for(int i=0;i<mySubjectBean.getWeekList().size();i++){
            weeklistStr+=mySubjectBean.getWeekList().get(i)+",";
        }
        ContentValues values = new ContentValues();
        values.put(CourseTable.CourseEntry.COLUMN_NAME_NAME,mySubjectBean.getName());
        values.put(CourseTable.CourseEntry.COLUMN_NAME_NUMBER,mySubjectBean.getCouNumber());
        values.put(CourseTable.CourseEntry.COLUMN_NAME_ROOM,mySubjectBean.getRoom());
        values.put(CourseTable.CourseEntry.COLUMN_NAME_TEACHER,mySubjectBean.getTeacher());
        values.put(CourseTable.CourseEntry.COLUMN_NAME_WEEKS,weeklistStr);
        values.put(CourseTable.CourseEntry.COLUMN_NAME_DAY,mySubjectBean.getDay());
        values.put(CourseTable.CourseEntry.COLUMN_NAME_SATRT,mySubjectBean.getStart());
        values.put(CourseTable.CourseEntry.COLUMN_NAME_STEP,mySubjectBean.getStep());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(CourseTable.CourseEntry.TABLE_NAME,values,"_id=?",new String[]{String.valueOf(mySubjectBean.get_id())});
/*        db.execSQL("UPDATE courseTable SET name =?,couNumber=?,room=?,teacher=?,weeklist=?,start=?,step=?,day=? WHERE _id = ?",
                new String[]{mySubjectBean.getName(),mySubjectBean.getCouNumber(),mySubjectBean.getRoom(),
                        mySubjectBean.getTeacher(),String.valueOf(mySubjectBean.getWeekList().get(0)),weeklistStr,
                        String.valueOf(mySubjectBean.getDay()),String.valueOf(mySubjectBean.get_id())});*/
    }


}
