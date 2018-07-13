package com.example.shq.subjecttimetable.other;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shq.subjecttimetable.R;
import com.zhuangfei.timetable.core.SubjectBean;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by shq on 2018/7/4.
 */

public class ListCoursePopup extends BasePopupWindow {
    private ListView mListView;
    private OnListPopupItemClickListener mOnListPopupItemClickListener;

    private ListCoursePopup(Activity context) {
        super(context);
    }

    public ListCoursePopup(Activity context, Builder builder) {
        this(context);
        mListView = (ListView) findViewById(R.id.popup_list);
        setAdapter(context, builder);
    }

    public static class Builder {
        private List<MySubjectBean> mItemEventList = new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }

        public Builder addItem(MySubjectBean itemTx) {
            mItemEventList.add(itemTx);
            return this;
        }

/*        public Builder addItem(int clickTag, MySubjectBean itemTx) {
            mItemEventList.add(new clickItemEvent(clickTag, itemTx));
            return this;
        }*/

        public List<MySubjectBean> getItemEventList() {
            return mItemEventList;
        }

        public ListCoursePopup build() {
            return new ListCoursePopup(mContext, this);
        }

    }

    public static class clickItemEvent {
        private int clickTag;
        private MySubjectBean itemTx;

        public clickItemEvent(int clickTag, MySubjectBean itemTx) {
            this.clickTag = clickTag;
            this.itemTx = itemTx;
        }

        public int getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public MySubjectBean getItemTx() {
            return itemTx;
        }

        public void setItemTx(MySubjectBean itemTx) {
            this.itemTx = itemTx;
        }
    }

    //=============================================================adapter
    class ListPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<MySubjectBean> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<MySubjectBean> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public MySubjectBean getItem(int position) {
            if (mItemList.get(position) instanceof MySubjectBean) {
                return mItemList.get(position);
            }
        /*    if (mItemList.get(position) instanceof clickItemEvent) {
                return ((clickItemEvent) mItemList.get(position)).getItemTx();
            }*/
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_course, parent, false);
                vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                vh.tv_room = (TextView) convertView.findViewById(R.id.tv_room);
                vh.tv_week = (TextView) convertView.findViewById(R.id.tv_week);
                vh.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
                vh.tv_teacher = (TextView) convertView.findViewById(R.id.tv_teacher);
                vh.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv_name.setText(getItem(position).getName());
            vh.tv_room.setText(getItem(position).getRoom());
            String tvDay="";
            switch (getItem(position).getDay()){
                case 1:
                    tvDay+="周一 ";
                    break;
                case 2:
                    tvDay+="周二 ";
                    break;
                case 3:
                    tvDay+="周三 ";
                    break;
                case 4:
                    tvDay+="周四 ";
                    break;
                case 5:
                    tvDay+="周五 ";
                    break;
                case 6:
                    tvDay+="周六 ";
                    break;
                case 7:
                    tvDay+="周日 ";
                    break;
                default:
                    tvDay+=" ";
                    break;
            }
            tvDay +=getItem(position).getStart()+"-";
            int k=getItem(position).getStart();
            for(int i=1;i<getItem(position).getStep();i++){
                k++;
            }
            tvDay+=k+"节";
            vh.tv_week.setText(getItem(position).getWeekList().get(0)+"-"+getItem(position).getWeekList().get(getItem(position).getWeekList().size()-1)+"周");

            vh.tv_day.setText(tvDay);

            vh.tv_teacher.setText(getItem(position).getTeacher());
            vh.tv_number.setText(getItem(position).getCouNumber());
            return convertView;
        }

        public List<MySubjectBean> getItemList() {
            return this.mItemList;
        }


        class ViewHolder {
            public TextView tv_name;
            public TextView tv_room;
            public TextView tv_week;
            public TextView tv_day;
            public TextView tv_teacher;
            public TextView tv_number;
        }
    }

    //=============================================================init
    private void setAdapter(Activity context, Builder builder) {
        if (builder.getItemEventList() == null || builder.getItemEventList().size() == 0) return;
        final ListPopupAdapter adapter = new ListPopupAdapter(context, builder.getItemEventList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnListPopupItemClickListener != null) {
                    Object clickObj = adapter.getItemList().get(position);
                    if (clickObj instanceof SubjectBean) {
                        mOnListPopupItemClickListener.onItemClick(position);
                    }
                    if (clickObj instanceof clickItemEvent) {
                        int what = ((clickItemEvent) clickObj).clickTag;
                        mOnListPopupItemClickListener.onItemClick(what);
                    }
                }
            }
        });

    }

    //=============================================================super methods
    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public Animator initShowAnimator() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(initAnimaView(), "rotationX", 90f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(initAnimaView(), "translationY", 250f, 0f).setDuration(400),
                ObjectAnimator.ofFloat(initAnimaView(), "alpha", 0f, 1f).setDuration(400 * 3 / 2));
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_list_course);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
   public Animator initExitAnimator() {
       AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(initAnimaView(), "rotationX", 0f, 90f).setDuration(400),
                ObjectAnimator.ofFloat(initAnimaView(), "translationY", 0f, 250f).setDuration(400),
                ObjectAnimator.ofFloat(initAnimaView(), "alpha", 1f, 0f).setDuration(400 * 3 / 2));
        return set;
    }

    //=============================================================interface

    public OnListPopupItemClickListener getOnListPopupItemClickListener() {
        return mOnListPopupItemClickListener;
    }

    public void setOnListPopupItemClickListener(OnListPopupItemClickListener onListPopupItemClickListener) {
        mOnListPopupItemClickListener = onListPopupItemClickListener;
    }

    public interface OnListPopupItemClickListener {
        void onItemClick(int what);
    }
}
