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
 * Created by shq on 2018/7/9.
 */

public class ListGradePopup extends BasePopupWindow {
    private ListView mListView;
    private OnListPopupItemClickListener mOnListPopupItemClickListener;

    private ListGradePopup(Activity context) {
        super(context);
    }

    public ListGradePopup(Activity context, Builder builder) {
        this(context);
        mListView = (ListView) findViewById(R.id.popup_list);
        setAdapter(context, builder);
    }

    public static class Builder {
        private List<Grade> mItemEventList = new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }

        public Builder addItem(Grade itemTx) {
            mItemEventList.add(itemTx);
            return this;
        }

/*        public Builder addItem(int clickTag, Grade itemTx) {
            mItemEventList.add(new clickItemEvent(clickTag, itemTx));
            return this;
        }*/

        public List<Grade> getItemEventList() {
            return mItemEventList;
        }

        public ListGradePopup build() {
            return new ListGradePopup(mContext, this);
        }

    }

    public static class clickItemEvent {
        private int clickTag;
        private Grade itemTx;

        public clickItemEvent(int clickTag, Grade itemTx) {
            this.clickTag = clickTag;
            this.itemTx = itemTx;
        }

        public int getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public Grade getItemTx() {
            return itemTx;
        }

        public void setItemTx(Grade itemTx) {
            this.itemTx = itemTx;
        }
    }

    //=============================================================adapter
    class ListPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<Grade> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<Grade> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public Grade getItem(int position) {
            if (mItemList.get(position) instanceof Grade) {
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
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_grade, parent, false);
                vh.tv_xuefen = (TextView) convertView.findViewById(R.id.tv_xuefen);
                vh.tv_grade = (TextView) convertView.findViewById(R.id.tv_grade);
                vh.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.tv_xuefen.setText(getItem(position).getXuefen());
            vh.tv_grade.setText(getItem(position).getChengji());
            vh.tv_name.setText(getItem(position).getName());

            return convertView;
        }

        public List<Grade> getItemList() {
            return this.mItemList;
        }


        class ViewHolder {
            private TextView tv_name;
            public TextView tv_xuefen;
            public TextView tv_grade;
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
        return createPopupById(R.layout.popup_list_grade);
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
