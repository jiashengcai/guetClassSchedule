package com.example.shq.subjecttimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shq.subjecttimetable.helper.CourseTableHelper;
import com.example.shq.subjecttimetable.helper.SharedPref;

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

public class showScore extends AppCompatActivity {
    private String xuefenj;
    public SharedPref sharedPref;
    private Button button1,button2,button3,button4,button5;
    private OkHttpClient client;
    private final String TAG="test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        sharedPref = SharedPref.getInstance(getApplicationContext());
        button1=(Button)findViewById(R.id.xuefenji1);
        button2=(Button)findViewById(R.id.xuefenji2);
        button3=(Button)findViewById(R.id.xuefenji3);
        button4=(Button)findViewById(R.id.xuefenji0);
        final TextView textView=(TextView)findViewById(R.id.ffscore) ;
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showXueFenJi(1);
                button1.setText("2015-2016："+xuefenj);
                textView.setText(xuefenj);
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showXueFenJi(2);
                button2.setText("2016-2017："+xuefenj);
                textView.setText(xuefenj);
            }
        });
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showXueFenJi(3);
                button3.setText("2017-2018："+xuefenj);
                textView.setText(xuefenj);
            }
        });
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showXueFenJi(0);
                button4.setText("入学至今："+xuefenj);
                textView.setText(xuefenj);
            }
        });
    }

    public void showXueFenJi(final int type){
        // http://172.16.64.236/student/xuefenji.asp?type=0&lwPageSize=1000&lwBtnquery=%B2%E9%D1%AF
        client=new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                        cookieStore.put(HttpUrl.parse("http://xk.cacacai.cn:8080/student/public/login.asp"), cookies);
                        for(Cookie cookie:cookies){
                            System.out.println("cookie Name:"+cookie.name());
                            System.out.println("cookie Path:"+cookie.path());
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://xk.cacacai.cn:8080/student/public/login.asp"));
                        if(cookies==null){
                            System.out.println("没加载到cookie");
                        }
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();//设置保存cookie

        RequestBody formBody = new FormBody.Builder()
                .add("username", sharedPref.getString("username",null))
                .add("passwd", sharedPref.getString("password",null))
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {//登陆完成
                /*int i=0;
                while (i<=1){
                    Log.d("test",i+"");

                }*/
                getCredit(call,response,type);
            }
        });
    }

    private void getCredit(Call call, Response response, final int type){
        final String xn;
        if(type==1) xn="2015-2016";
        else if(type==2) xn="2016-2017";
        else if(type==3) xn="2017-2018";
        else xn="";
        RequestBody formBody1 = new FormBody.Builder()
                .add("xn",xn)
                .add("lwPageSize", "1000")
                .add("lwBtnquery","%B2%E9%D1%AF")
                .build();//创建网络请求表单
        Request request = new Request.Builder()
                .url(getApplicationContext().getString(R.string.url_xuefenji))
                .post(formBody1)
                .build();//创建网络请求request
        call = client.newCall(request);
        /*if (1 > i){
            i++;
            return;
        }*/
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                byte[] data=response.body().bytes();
                String str = new String(data, "gb2312");
                List<String> stringList;
                //if(type==0) {
                    stringList = CourseTableHelper.getAllSatisfyStr(str,
                            "<B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font face='黑体'>(.*?)</font></B></td><th>(.*?)</th></tr>");
                    Log.d("!!!!!!!!!!!!!!!!!1**",stringList.toString());
                    xuefenj = CourseTableHelper.subString(stringList.get(0), "face='黑体'>", "</font>");
                /*}else if(type==1){
                    stringList = CourseTableHelper.getAllSatisfyStr(str,
                            "<B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font face='黑体'>(.*?)</font></B></td><th>2015-2016学年</th></tr>");
                    xuefenj = CourseTableHelper.subString(stringList.get(0), "face='黑体'>", "</font>");
                }
                else if(type==2){
                    stringList = CourseTableHelper.getAllSatisfyStr(str,
                            "<B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font face='黑体'>(.*?)</font></B></td><th>2016-2017学年</th></tr>");
                    xuefenj = CourseTableHelper.subString(stringList.get(0), "face='黑体'>", "</font>");

                }
                else{
                    stringList = CourseTableHelper.getAllSatisfyStr(str,
                            "<B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font face='黑体'>(.*?)</font></B></td><th>2017-2018学年</th></tr>");
                    xuefenj = CourseTableHelper.subString(stringList.get(0), "face='黑体'>", "</font>");
                }*/
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        return strXFJ;
                    }
                });*/
                response.body().close();
            }
        });
    }
}
