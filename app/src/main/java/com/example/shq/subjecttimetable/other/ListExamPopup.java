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

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by shq on 2018/7/7.
 */

public class ListExamPopup  extends BasePopupWindow {
    private ListView mListView;
    private ListExamPopup.OnListPopupItemClickListener mOnListPopupItemClickListener;

    private ListExamPopup(Activity context) {
        super(context);
    }

    public ListExamPopup(Activity context, Builder builder) {
        this(context);
        mListView = (ListView) findViewById(R.id.popup_list);
        setAdapter(context, builder);
    }

    public static class Builder {
        private List<Exam> mItemEventList = new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }

        public Builder addItem(Exam itemTx) {
            mItemEventList.add(itemTx);
            return this;
        }

/*        public Builder addItem(int clickTag, Exam itemTx) {
            mItemEventList.add(new clickItemEvent(clickTag, itemTx));
            return this;
        }*/

        public List<Exam> getItemEventList() {
            return mItemEventList;
        }

        public ListExamPopup build() {
            return new ListExamPopup(mContext, this);
        }

    }

    public static class clickItemEvent {
        private int clickTag;
        private Exam itemTx;

        public clickItemEvent(int clickTag, Exam itemTx) {
            this.clickTag = clickTag;
            this.itemTx = itemTx;
        }

        public int getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public Exam getItemTx() {
            return itemTx;
        }

        public void setItemTx(Exam itemTx) {
            this.itemTx = itemTx;
        }
    }

    //=============================================================adapter
    class ListPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<Exam> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<Exam> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public Exam getItem(int position) {
            if (mItemList.get(position) instanceof Exam) {
                return mItemList.get(position);
            }
            /*if (mItemList.get(position) instanceof clickItemEvent) {
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
            ListPopupAdapter.ViewHolder vh = null;
            if (convertView == null) {
                vh = new ListPopupAdapter.ViewHolder();
                convertView = mInflater.inflate(R.layout.item_exam, parent, false);
                vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                vh.tv_room = (TextView) convertView.findViewById(R.id.tv_room);
                vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                vh.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                convertView.setTag(vh);
            } else {
                vh = (ListPopupAdapter.ViewHolder) convertView.getTag();
            }
            vh.tv_name.setText(getItem(position).getCourseName());
            vh.tv_room.setText(getItem(position).getRoom());
            String time=null;
            time =getItem(position).getWeek()+"周 ";
            switch (getItem(position).getDay()){
                case 1:
                    time+="周一 ";
                    break;
                case 2:
                    time+="周二 ";
                    break;
                case 3:
                    time+="周三 ";
                    break;
                case 4:
                    time+="周四 ";
                    break;
                case 5:
                    time+="周五 ";
                    break;
                case 6:
                    time+="周六 ";
                    break;
                case 7:
                    time+="周日 ";
                    break;
                default:
                    time+=" ";
                    break;
            }
            time+=getItem(position).getTime();
            vh.tv_time.setText(time);
            vh.tv_number.setText(getItem(position).getCourseNumber());
            return convertView;
        }

        public List<Exam> getItemList() {
            return this.mItemList;
        }


        class ViewHolder {
            public TextView tv_name;
            public TextView tv_time;
            public TextView tv_room;
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
                    if (clickObj instanceof String) {
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
        return createPopupById(R.layout.popup_list_exam);
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
