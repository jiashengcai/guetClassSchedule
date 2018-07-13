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

    private String web1="http://www.guet.edu.cn/";
    private String web2="http://bkjw2.guet.edu.cn/";
    private String web3="http://bkjw.guet.edu.cn/";
    private String web4="http://cwcx.guet.edu.cn/xfzxqcx/Account/Login?ReturnUrl=%2fxfzxqcx%2fVXSXM";
    private String web5="https://passport.etest.net.cn/CETLogin?ReturnUrl=http://cet.etest.net.cn/Home/VerifyPassport/?LoginType=0/";
    private String web6="http://cet.neea.edu.cn/cet/";
    private String web7="http://xk.cacacai.cn:8080/student/public/login.asp/";

    private Button button1,button2,button3,button4,button5,button6,button7;
    public static F4 newInstance() {
        F4 fragment = new F4();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_f4, container, false);
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
        button4 = (Button) view.findViewById(R.id.button4);
        button5 = (Button) view.findViewById(R.id.button5);
        button6 = (Button) view.findViewById(R.id.button6);
        button7 = (Button) view.findViewById(R.id.button7);
        button1.setOnClickListener(new View.OnClickListener()
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
        button7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent sIntent = new Intent(getActivity(), Web1.class);
                sIntent.putExtra(Web1.RETURN_INFO, web7);
                startActivity(sIntent);
            }
        });
        return view;
    }
}
