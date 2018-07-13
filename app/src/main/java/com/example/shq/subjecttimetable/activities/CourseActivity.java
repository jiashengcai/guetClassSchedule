package com.example.shq.subjecttimetable.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.shq.subjecttimetable.R;
import com.example.shq.subjecttimetable.helper.CourseTableHelper;
import com.example.shq.subjecttimetable.helper.MyDbOpenHelper;
import com.example.shq.subjecttimetable.helper.SharedPref;
import com.example.shq.subjecttimetable.helper.ToastHelper;
import com.example.shq.subjecttimetable.other.MySubjectBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_confirm;
    public Button bt_delete;

    private EditText ET_courseName;
    private EditText ET_room;
    private TextView TV_week;
    private TextView TV_day;
    private TextView TV_step;
    private EditText ET_teahcer;
    private EditText ET_courseNumber;

    private String action;
    private String _id = "";

    private MyDbOpenHelper myDbHelper;
    private SharedPref sharedPref;
    private MySubjectBean mySubjectBeanTemp;

    private String[] items;
    private List<Integer> checkedWeekItems;
    private List<Integer> checkedStepItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);


        mySubjectBeanTemp = new MySubjectBean();
        //region 获取控件，初始化myDbHelper、sharedPref
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        bt_delete=(Button)findViewById(R.id.bt_delete);

        ET_courseName = (EditText) findViewById(R.id.ET_courseName);
        ET_room = (EditText) findViewById(R.id.ET_room);
        ET_teahcer = (EditText) findViewById(R.id.ET_teahcer);
        ET_courseNumber = (EditText) findViewById(R.id.ET_courseNumber);

        TV_week = (TextView) findViewById(R.id.TV_week);
        TV_day = (TextView) findViewById(R.id.TV_day);
        TV_step = (TextView) findViewById(R.id.TV_step);
        myDbHelper = new MyDbOpenHelper(this);
        sharedPref = SharedPref.getInstance(getApplicationContext());
        //endregion

        ET_courseName.setText("");
        ET_room.setText("");
        ET_teahcer.setText("");
        ET_courseNumber.setText("");
        TV_week.setText("");
        TV_day.setText("");
        TV_step.setText("");

        tv_back.setOnClickListener(this);
        TV_week.setOnClickListener(this);
        TV_day.setOnClickListener(this);
        TV_step.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);
        bt_delete.setOnClickListener(this);

        //新页面接收数据,接收action值
        Bundle bundle = this.getIntent().getExtras();
        action = bundle.getString("action");


        //region 初始化控件内容
        if (action.compareTo("add") == 0) {
            bt_delete.setVisibility(View.INVISIBLE);
            tv_title.setText("添加课程");
            tv_confirm.setText("添加");
            TV_day.setText("请选择星期");
            TV_week.setText("请选择上课周");
            TV_step.setText("请选择上课节次");
        } else if (action.compareTo("edit") == 0) {
            bt_delete.setVisibility(View.VISIBLE);
            tv_title.setText("修改课程");
            tv_confirm.setText("修改");
            String id = bundle.getString("id");
            _id = id;
            MySubjectBean mySubjectBean = myDbHelper.getCourseById(Integer.parseInt(id));
            mySubjectBeanTemp.set_id(mySubjectBean.get_id());
            mySubjectBeanTemp.setName(mySubjectBean.getName());
            mySubjectBeanTemp.setCouNumber(mySubjectBean.getCouNumber());
            mySubjectBeanTemp.setRoom(mySubjectBean.getRoom());
            mySubjectBeanTemp.setTeacher(mySubjectBean.getTeacher());
            mySubjectBeanTemp.setWeekList(mySubjectBean.getWeekList());
            mySubjectBeanTemp.setStart(mySubjectBean.getStart());
            mySubjectBeanTemp.setStep(mySubjectBean.getStep());
            mySubjectBeanTemp.setDay(mySubjectBean.getDay());
            ET_courseName.setText(mySubjectBean.getName());
            ET_room.setText(mySubjectBean.getRoom());
            ET_teahcer.setText(mySubjectBean.getTeacher());
            ET_courseNumber.setText(mySubjectBean.getCouNumber());
            String weekStr="";
            for (int i=0;i<mySubjectBean.getWeekList().size()-1;i++) {
                weekStr+=mySubjectBean.getWeekList().get(i)+"、";
            }
            weekStr+=mySubjectBean.getWeekList().get(mySubjectBean.getWeekList().size()-1)+"周";
            TV_week.setText(weekStr);
            //TV_week.setText(mySubjectBean.getWeekList().get(0) + "-" + mySubjectBean.getWeekList().get(mySubjectBean.getWeekList().size() - 1));
            String[] temp = getResources().getStringArray(R.array.weekArrs);
            checkedWeekItems = new ArrayList<>();
            for (int i = 0; i < mySubjectBean.getWeekList().size(); i++) {
                int k = mySubjectBean.getWeekList().get(i);
                checkedWeekItems.add(k - 1);
            }
            switch (mySubjectBean.getDay()) {
                case 1:
                    TV_day.setText("星期一");
                    break;
                case 2:
                    TV_day.setText("星期二");
                    break;
                case 3:
                    TV_day.setText("星期三");
                    break;
                case 4:
                    TV_day.setText("星期四");
                    break;
                case 5:
                    TV_day.setText("星期五");
                    break;
                case 6:
                    TV_day.setText("星期六");
                    break;
                case 7:
                    TV_day.setText("星期天");
                    break;
                default:
                    TV_day.setText("");
                    break;
            }
            String tvS = mySubjectBean.getStart() + "-";
            int k = mySubjectBean.getStart();
            checkedStepItems = new ArrayList<>();
            checkedStepItems.add(k);
            for (int i = 1; i < mySubjectBean.getStep(); i++) {
                k++;
                checkedStepItems.add(k);
            }
            tvS += k + "节";
            TV_step.setText(tvS);
        }
        //endregion
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TV_day:
                showAlertDialog("day");
                break;
            case R.id.TV_week:
                showAlertDialog("week");
                break;
            case R.id.TV_step:
                showAlertDialog("step");
                break;
            case R.id.tv_confirm:
                if(action.compareTo("add")==0)
                    addCourseTable();
                else if(action.compareTo("edit")==0)
                    editCourseTable();
                break;
            case R.id.tv_back:
                finish();
                System.exit(0);
                break;
            case R.id.bt_delete:
                myDbHelper.deleteCourseTableById(mySubjectBeanTemp.get_id());
                ToastHelper.showToast(this,"删除成功！",0);
                new Handler().postDelayed(new Runnable(){ public void run() { //execute the task
                    //
                } }, 2000);
                finish();
                System.exit(0);
            default:
                break;
        }
    }

    private void showAlertDialog(String str) {
        final List<Integer> checkedItems = new ArrayList<>();
        AlertDialog dialog = null;
        boolean[] cdItemTemp = null;
        switch (str) {
            case "day":

                //region 选择星期对话框
                items = getResources().getStringArray(R.array.dayArrs);
                dialog = new AlertDialog.Builder(this).setTitle("选择上课星期").setIcon(R.drawable.ic_week)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mySubjectBeanTemp.setDay(which+1);
                                dialog.dismiss();
                                TV_day.setText(items[which]);
                            }
                        }).create();
                dialog.show();
                break;
            //endregion
            case "week":
                //region 周次对话框
                items = getResources().getStringArray(R.array.weekArrs);
/*                cdItemTemp = new boolean[items.length];
                if (mySubjectBeanTemp.getWeekList()!= null){
                    for (int i = 0; i < mySubjectBeanTemp.getWeekList().size(); i++) {
                        cdItemTemp[mySubjectBeanTemp.getWeekList().get(i)] = true;
                    }
                } else {
                    if (action.compareTo("edit") == 0)
                        for (int i = 0; i < checkedWeekItems.size(); i++) {
                            cdItemTemp[checkedWeekItems.get(i)] = true;
                        }
                }*/
                dialog = new AlertDialog.Builder(this).setTitle("选择上课周数").setIcon(R.drawable.ic_week)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Integer> integerList = new ArrayList<>();
                                mySubjectBeanTemp.setWeekList(integerList);
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mySubjectBeanTemp.setWeekList(checkedItems);
                                CourseTableHelper.sort(checkedItems);
                                String weekStr="";
                                for (int i=0;i<checkedItems.size()-1;i++) {
                                    weekStr+=checkedItems.get(i)+"、";
                                }
                                weekStr+=checkedItems.get(checkedItems.size()-1)+"周";
                                TV_week.setText(weekStr);
                            }
                        })
                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(checkedWeekItems==null)
                                    checkedWeekItems=new ArrayList<>();
                                if (isChecked) {
                                    checkedWeekItems.add(which + 1);
                                    checkedItems.add(which + 1);
                                }else {
                                    for (int i=0;i<checkedItems.size();i++){
                                        if(checkedItems.get(i)==which+1)
                                            checkedItems.remove(i);
                                    }
                                }
                            }
                        }).create();
                dialog.show();
                if(checkedWeekItems!=null)
                    checkedWeekItems.clear();
                break;
            //endregion
            case "step":
                //region 节次对话框
                items = getResources().getStringArray(R.array.stepArrs);
               /* cdItemTemp = new boolean[items.length];
                if (action.compareTo("edit") == 0)
                    for (int i = 0; i < checkedStepItems.size(); i++) {
                        cdItemTemp[checkedStepItems.get(i)] = true;
                    }*/
                dialog = new AlertDialog.Builder(this).setTitle("选择上课节次").setIcon(R.drawable.ic_day)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Integer> integerList = new ArrayList<>();
                                mySubjectBeanTemp.setWeekList(integerList);
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mySubjectBeanTemp.setStart(checkedItems.get(0));
                                mySubjectBeanTemp.setStep(checkedItems.size());
                                TV_step.setText(checkedItems.get(0)+"-"+checkedItems.get(checkedItems.size()-1)+"节");
                            }
                        })
                        .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(checkedStepItems==null)
                                    checkedStepItems=new ArrayList<>();
                                if (isChecked) {
                                    checkedStepItems.add(which + 1);
                                    checkedItems.add(which + 1);
                                } else {
                                    /*for()*/
                                }
                            }
                        }).create();
                dialog.show();
                if(checkedStepItems!=null)
                    checkedStepItems.clear();
                //endregion
                break;
        }
    }

    private void addCourseTable(){
        mySubjectBeanTemp.setName(ET_courseName.getText().toString());
        mySubjectBeanTemp.setCouNumber(ET_courseNumber.getText().toString());
        mySubjectBeanTemp.setTeacher(ET_teahcer.getText().toString());
        mySubjectBeanTemp.setRoom(ET_room.getText().toString());
        if(mySubjectBeanTemp.getDay()==0||mySubjectBeanTemp.getWeekList()==null){
            ToastHelper.showToast(this,"上课周次或星期不能为空！",0);
            return;
        }
        myDbHelper.addCourseTable(mySubjectBeanTemp,sharedPref.getString("username",null));
        ToastHelper.showToast(this,"添加成功！",0);
        new Handler().postDelayed(new Runnable(){ public void run() { //execute the task
            //
        } }, 2000);
        finish();
        System.exit(0);
    }
    private void editCourseTable(){
        mySubjectBeanTemp.setName(ET_courseName.getText().toString());
        mySubjectBeanTemp.setCouNumber(ET_courseNumber.getText().toString());
        mySubjectBeanTemp.setTeacher(ET_teahcer.getText().toString());
        mySubjectBeanTemp.setRoom(ET_room.getText().toString());
        if(mySubjectBeanTemp.getDay()==0||mySubjectBeanTemp.getWeekList()==null){
            ToastHelper.showToast(this,"上课周次或星期不能为空！",0);
            return;
        }
        myDbHelper.updateCourseById(mySubjectBeanTemp);
        ToastHelper.showToast(this,"修改成功！",0);
        new Handler().postDelayed(new Runnable(){ public void run() { //execute the task
            //
            } }, 2000);

        finish();
        System.exit(0);
    }

}
