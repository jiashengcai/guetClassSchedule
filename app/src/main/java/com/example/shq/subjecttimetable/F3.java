package com.example.shq.subjecttimetable;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shq.subjecttimetable.helper.CourseTableHelper;
import com.example.shq.subjecttimetable.helper.SharedPref;
import com.example.shq.subjecttimetable.helper.ToastHelper;

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

public class F3 extends Fragment {
    private String course;
    private TextView courseNum;
    private TextView result;
    private List<Cookie> cookies;//login.asp页面cookie
    private HandlerThread thread;
    private Handler mHandler;
    public SharedPref sharedPref;
    private OkHttpClient client;
    private final String TAG="test";
    private HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();//用户cookie
    public static F3 newInstance() {
        F3 fragment = new F3();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_f3, container, false);
        sharedPref = SharedPref.getInstance(getActivity().getApplicationContext());
        courseNum=view.findViewById(R.id.courseNum);
        result=(TextView) view.findViewById(R.id.choice_result);
        Button select=view.findViewById(R.id.select);
        thread=new HandlerThread("select");
        thread.start();
        mHandler = new Handler(thread.getLooper());//使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
        select.setOnClickListener(new View.OnClickListener()
        {//
            @Override
            public void onClick(View view) {
                course=courseNum.getText().toString().trim();
                if (!course.equals(null)){
                    login(result,course);
                }else{
                    Log.d(TAG,course);
                    result.setText("输入不能为空");
                }
            }
        });
        return view;
    }

    Runnable select=new Runnable() {
        @Override
        public void run() {
            boolean isRun=true;
            try {
                while (true){
                    String result2;
                    result2=select();//选课
                    Log.d(TAG,result2);
                    if (result2.indexOf("错误")!=-1){
                        Log.d(TAG,"错误");
                        isRun=false;
                        //mHandler.removeCallbacks(select);
                        break;
                    }

                    Thread.sleep(1000);
                    // result.setText(result2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public void login( TextView result, String course){
        // http://172.16.64.236/student/xuefenji.asp?type=0&lwPageSize=1000&lwBtnquery=%B2%E9%D1%AF
        /*"http://bkjw2.guet.edu.cn/student/select.asp";
          $postFile="spno=000000&selecttype=".$selectType."&testtime=&course=".$classCode."&".$textbookCode."=0&lwBtnselect=%CC%E1%BD%BB";*/
        client=new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
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
                        cookies= cookieStore.get(HttpUrl.parse("http://xk.cacacai.cn:8080/student/public/login.asp"));
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
                ToastHelper.showToast(getActivity().getApplicationContext(), "网络请求错误！", 0);
            }
            @Override
            public void onResponse(Call call, Response response) {//模拟登陆成功
                //开启线程
                mHandler.post(select);
                response.body().close();
            }
        });
    }



    private String select() throws IOException {
        final String[] result = {"错误"};
        Call call;//spno=000000&selecttype=%D5%FD%B3%A3&testtime=&course=1811964&textbook1811964=0&lwBtnselect=%CC%E1%BD%BB
        RequestBody formBody1 = new FormBody.Builder()
                .add("spno", "000000")//通识教育
                .add("selecttype", "%D6%D8%D0%DE")//正常/重修
                .add("testtime","")
                .add("course",course)//课号，非课程代码
                .add("textbook"+course,"0")//教材
                .add("lwBtnselect","%CC%E1%BD%BB")//提交
                .build();//创建网络请求表单
        Request request = new Request.Builder()
                .url(getActivity().getApplicationContext().getString(R.string.url_select))
                .post(formBody1)
                .build();//创建网络请求request
        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastHelper.showToast(getActivity().getApplicationContext(), "网络请求错误！", 0);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                byte[] data=response.body().bytes();
                String str = new String(data, "gb2312");
                List<String> stringList= CourseTableHelper.getAllSatisfyStr(str,
                        "<font (.*?)</font>");
                Log.d(TAG,stringList.get(0));
                result[0]=stringList.get(0);
                response.body().close();
            }
        });
        return result[0];
    }
}
