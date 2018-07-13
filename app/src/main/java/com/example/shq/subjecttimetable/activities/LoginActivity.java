package com.example.shq.subjecttimetable.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shq.subjecttimetable.R;
import com.example.shq.subjecttimetable.helper.CourseTableHelper;
import com.example.shq.subjecttimetable.helper.MyDbOpenHelper;
import com.example.shq.subjecttimetable.helper.SharedPref;
import com.example.shq.subjecttimetable.helper.ToastHelper;
import com.example.shq.subjecttimetable.other.MySubjectBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {



    //region 定义
    public EditText et_username;
    public EditText et_password;
    public Button bt_username_clear;
    public Button bt_pwd_clear;
    public Button bt_login;


    public TextWatcher username_watcher;
    public TextWatcher password_watcher;


    public SharedPref sharedPref;

    private String username;
    private String password;

    private MyDbOpenHelper myDbOpenHelper;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region 初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username=(EditText)findViewById(R.id.et_username);
        et_password=(EditText)findViewById(R.id.et_password);
        bt_username_clear=(Button)findViewById(R.id.bt_username_clear);
        bt_pwd_clear=(Button)findViewById(R.id.bt_pwd_clear);
        bt_login=(Button)findViewById(R.id.bt_login);
        et_username.requestFocus();
        initWatcher();
        bt_username_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setText("");
                et_password.setText("");
            }
        });
        bt_pwd_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_password.setText("");
            }
        });
        et_username.addTextChangedListener(username_watcher);
        et_password.addTextChangedListener(password_watcher);
        sharedPref = SharedPref.getInstance(getApplicationContext());
        et_username.setText(sharedPref.getString("username",null));
        et_password.setText(sharedPref.getString("password",null));
        myDbOpenHelper=new MyDbOpenHelper(getApplicationContext());
        //endregion

        //region button点击事件
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                if (username.trim().isEmpty()) {
                    ToastHelper.showToast(getApplicationContext(), "请输入用户名！", 0);
                    return;
                }
                if (password.trim().isEmpty()) {
                    ToastHelper.showToast(getApplicationContext(), "请输入密码！", 0);
                    return;
                }
                if(username.compareTo(myDbOpenHelper.getUser(username,password))==0){
                    sharedPref.putString("username", username);
                    sharedPref.putString("password", password);
                    Intent intent=new  Intent();
                    intent.setClass(LoginActivity.this,fragment.class);
                    startActivity(intent);
                    return;
                }
                getCourseTable();
            }
        });
        //endregion
    }




    private void getCourseTable() {
        final OkHttpClient client;
        client=new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                        cookieStore.put(HttpUrl.parse("http://xk.cacacai.cn:8080/student/public/login.asp"), cookies);
                        Log.d("test","加载到cookie");
                        /*for(Cookie cookie:cookies){
                            System.out.println("cookie Name:"+cookie.name());
                            System.out.println("cookie Path:"+cookie.path());
                        }*/
                    }
                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {//登陆页没有cookie
                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://xk.cacacai.cn:8080/student/public/login.asp"));
                        /*if(cookies==null){
                            System.out.println("没加载到cookie");
                        }*/
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();//设置保存cookie

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("passwd", password)
                .add("login","%B5%C7%A1%A1%C2%BC")
                .add("mCode","000703")
                .build();//创建网络请求表单

        final Request request = new Request.Builder()
                .url(this.getString(R.string.url_login))
                .post(formBody)
                .build();//创建网络请求request
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                ToastHelper.showToast(getApplicationContext(), "网络请求错误！", 0);
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sharedPref.putString("username", username);
                sharedPref.putString("password", password);
                RequestBody formBody1 = new FormBody.Builder()
                        .add("term", "2017-2018_2")
                        .build();
                Request request1 = new Request.Builder()
                        .url(getApplicationContext().getString(R.string.url_course))
                        .addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8")
                        .post(formBody1)
                        .build();
                call = client.newCall(request1);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastHelper.showToast(getApplicationContext(), "网络请求错误！", 0);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] data=response.body().bytes();

                        final String str = new String(data, "gb2312");
                        List<MySubjectBean> mySubjectBeans= CourseTableHelper.htmlStringToMySubjectBeanList(str);
                        myDbOpenHelper.addUser(username,password);
                        myDbOpenHelper.addCourseTable(mySubjectBeans,username);
                        response.body().close();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent=new  Intent();
                                intent.setClass(LoginActivity.this,TestFragment.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

        });
    }


    private void initWatcher() {
        username_watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                et_password.setText("");
                if(s.toString().length()>0){
                    bt_username_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
        password_watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }
}
