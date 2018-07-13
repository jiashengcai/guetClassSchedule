package com.example.shq.subjecttimetable;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shq.subjecttimetable.helper.CourseTableHelper;
import com.example.shq.subjecttimetable.helper.SharedPref;
import com.example.shq.subjecttimetable.helper.ToastHelper;
import com.example.shq.subjecttimetable.other.Exam;
import com.example.shq.subjecttimetable.other.Grade;
import com.example.shq.subjecttimetable.other.ListExamPopup;
import com.example.shq.subjecttimetable.other.ListGradePopup;
import com.example.shq.subjecttimetable.other.XuefenjiPopup;

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

public class F2 extends Fragment {
    public SharedPref sharedPref;
    private Button button1,button2,button3,button4,button5;
    private OkHttpClient client;
    public static F2 newInstance() {
        F2 fragment = new F2();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_f2, container, false);
        TextView textView1=(TextView)view.findViewById(R.id.sname);
        TextView textView2=(TextView)view.findViewById(R.id.textView2);
        TextView textView3=(TextView)view.findViewById(R.id.textView3);
        sharedPref = SharedPref.getInstance(getActivity().getApplicationContext());
        showname(textView1,textView2);

        textView3.setText(" "+sharedPref.getString("username",null));
        button1=(Button)view.findViewById(R.id.button);
        button2=(Button)view.findViewById(R.id.button2);
        button3=(Button)view.findViewById(R.id.button3);
        button4=(Button)view.findViewById(R.id.button4);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //Intent sIntent = new Intent(getActivity(), showScore.class);
                //startActivity(sIntent);
                showGrade();
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), showScore.class);
                startActivity(sIntent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showExamList();
            }
        });
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ToastHelper.showToast(getActivity().getApplicationContext(), "该功能暂未开放，敬请期待！", 0);
            }
        });
        return view;
    }

    private void showname(TextView textView1,TextView textView2) {
        // http://172.16.64.236/student/xuefenji.asp?type=0&lwPageSize=1000&lwBtnquery=%B2%E9%D1%AF
        final TextView t1=textView1;
        final TextView t2=textView2;
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
                            Log.d("test","No cookies");
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
            public void onResponse(Call call, Response response) throws IOException {


                RequestBody formBody1 = new FormBody.Builder().build();//创建网络请求表单
                Request request = new Request.Builder()
                        .url(getActivity().getApplicationContext().getString(R.string.url_info))
                        .post(formBody1)
                        .build();//创建网络请求request
                Log.d("test",request.toString());
                call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        byte[] data=response.body().bytes();
                        String str = new String(data, "gb2312");
                        List<String> stringList=CourseTableHelper.getAllSatisfyStr(str,
                                "<p>姓名:(.*?)</p>");
                        final String name=CourseTableHelper.subString(stringList.get(0),"<p>姓名:","</p>");
                        stringList=CourseTableHelper.getAllSatisfyStr(str, "<p>年级:(.*?)</p>");
                        final String grade=CourseTableHelper.subString(stringList.get(0),"<p>年级:","</p>");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                t1.setText(" "+name);
                                t2.setText(" "+grade+"级");
                            }
                        });
                        response.body().close();
                    }
                });
            }
        });
    }

    public void showXueFenJi(){
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
                getCredit(call,response);
            }
        });
    }

    private void getCredit(Call call, Response response){
        RequestBody formBody1 = new FormBody.Builder()
                .add("type", "0")
                .add("lwPageSize", "1000")
                .add("lwBtnquery","%B2%E9%D1%AF")
                .build();//创建网络请求表单
        Request request = new Request.Builder()
                .url(getActivity().getApplicationContext().getString(R.string.url_xuefenji))
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
                List<String> stringList= CourseTableHelper.getAllSatisfyStr(str,
                        "<B>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font face='黑体'>(.*?)</font></B></td><th>入学至今</th></tr>");
                final String strXFJ=CourseTableHelper.subString(stringList.get(0),"face='黑体'>","</font>");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showXuefenjiPopup(strXFJ);
                    }
                });
                response.body().close();
            }
        });
    }
    private void showXuefenjiPopup(String xueFenJi){
        XuefenjiPopup xuefenjiPopup=new XuefenjiPopup(this.getActivity(),xueFenJi);
        xuefenjiPopup.showPopupWindow();
    }
    public void showExamList(){
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.showToast(getActivity().getApplicationContext(), "网络请求错误！", 0);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                RequestBody formBody1 = new FormBody.Builder()
                        .add("type", "0")
                        .add("Size","1000")
                        .add("lwBtnquery","%B2%E9%D1%AF")
                        .build();
                Request request1 = new Request.Builder()
                        .url(getActivity().getApplicationContext().getString(R.string.url_exam))
                        .post(formBody1)
                        .build();
                call = client.newCall(request1);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastHelper.showToast(getActivity().getApplicationContext(), "请求考试安排错误！", 0);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] data=response.body().bytes();
                        String str = new String(data, "gb2312");
                        final List<Exam> examList=CourseTableHelper.htmlToExamList(str);
                        sharedPref.putString("stmlStrExam",str);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showPopupListExam(examList);
                            }
                        });
                        response.body().close();
                    }

                });
            }

        });
    }

    private void showPopupListExam(List<Exam> examList){
        ListExamPopup.Builder examBuilder=new ListExamPopup.Builder(this.getActivity());
        for (int i=0;i<examList.size();i++){
            examBuilder.addItem(examList.get(i));
        }
        ListExamPopup listExamPopup=new ListExamPopup(this.getActivity(),examBuilder);
        listExamPopup.showPopupWindow();
    }

    public void showGrade(){
        // http://172.16.64.236/student/xuefenji.asp?type=0&lwPageSize=1000&lwBtnquery=%B2%E9%D1%AF
        client=new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                        cookieStore.put(HttpUrl.parse("http://172.16.64.236/student/public/login.asp"), cookies);
                        for(Cookie cookie:cookies){
                            System.out.println("cookie Name:"+cookie.name());
                            System.out.println("cookie Path:"+cookie.path());
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse("http://172.16.64.236/student/public/login.asp"));
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
            public void onResponse(Call call, Response response) throws IOException {


                RequestBody formBody1 = new FormBody.Builder()
                        .add("type", "0")
                        .add("lwPageSize", "1000")
                        .add("lwBtnquery","%B2%E9%D1%AF")
                        .build();//创建网络请求表单
                Request request = new Request.Builder()
                        .url(getActivity().getApplicationContext().getString(R.string.url_score))
                        .post(formBody1)
                        .build();//创建网络请求request
                call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        byte[] data=response.body().bytes();
                        String str = new String(data, "gb2312");
                        final List<Grade> gradeList=CourseTableHelper.htmlToGradeList(str);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showGradePopup(gradeList);
                            }
                        });
                        response.body().close();
                    }
                });
            }
        });
    }
    private void showGradePopup(List<Grade> gradeList){
        ListGradePopup.Builder examBuilder=new ListGradePopup.Builder(this.getActivity());
        for (int i=0;i<gradeList.size();i++){
            examBuilder.addItem(gradeList.get(i));
        }
        ListGradePopup listExamPopup=new ListGradePopup(this.getActivity(),examBuilder);
        listExamPopup.showPopupWindow();
    }

}
