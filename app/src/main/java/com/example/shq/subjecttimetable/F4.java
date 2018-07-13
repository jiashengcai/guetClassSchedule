package com.example.shq.subjecttimetable;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

public class F4 extends Fragment {

    private String web1="http://www.baidu.com/";
    private String web2="http://www.guet.edu.cn/";
    private String web3="http://bkjw2.guet.edu.cn/";
    private String web4="http://bkjw.guet.edu.cn/";
    private String web5="http://cwcx.guet.edu.cn/xfzxqcx/Account/Login?ReturnUrl=%2fxfzxqcx%2fVXS/";
    private String web6="http://123.15.35.34:9876/pastransport/zwdQuery/zwd.html?from=groupmessage&isappinstalled=0/";

    private Button send2,button2,button3,button4,button5,button6;
    public static F4 newInstance() {
        F4 fragment = new F4();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_f4, container, false);
        send2 = (Button) view.findViewById(R.id.buttont);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
        button4 = (Button) view.findViewById(R.id.button4);
        button5 = (Button) view.findViewById(R.id.button5);
        button6 = (Button) view.findViewById(R.id.button6);
        send2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), Web1.class);
                sIntent.putExtra(Web1.RETURN_INFO, web1);
                startActivity(sIntent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), Web1.class);
                sIntent.putExtra(Web1.RETURN_INFO, web2);
                startActivity(sIntent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), Web1.class);
                sIntent.putExtra(Web1.RETURN_INFO, web3);
                startActivity(sIntent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), Web1.class);
                sIntent.putExtra(Web1.RETURN_INFO, web4);
                startActivity(sIntent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), Web1.class);
                sIntent.putExtra(Web1.RETURN_INFO, web5);
                startActivity(sIntent);
            }
        });
        button6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), Web1.class);
                sIntent.putExtra(Web1.RETURN_INFO, web6);
                startActivity(sIntent);
            }
        });
        return view;
    }
}
