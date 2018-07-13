package com.example.shq.subjecttimetable.other;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.shq.subjecttimetable.R;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by shq on 2018/7/7.
 */

public class XuefenjiPopup extends BasePopupWindow {

    private View popupView;
    private String xueFenJi;

    public XuefenjiPopup(Activity context,String xueFenJi) {
        super(context);
        this.xueFenJi=xueFenJi;
        bindEvent();
    }


    @Override
    protected Animation initShowAnimation() {
        return getDefaultScaleAnimation();
    }

    @Override
    protected Animation initExitAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_xuefenji, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }
    private void bindEvent() {
        if (popupView != null) {
            TextView textView=(TextView) popupView.findViewById(R.id.tv_xuefenji);
            textView.setText(xueFenJi);
        }

    }
}
