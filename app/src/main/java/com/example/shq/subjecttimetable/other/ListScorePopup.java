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

public class ListScorePopup extends BasePopupWindow {
    private ListView mListView;
    private ListScorePopup.OnListPopupItemClickListener mOnListPopupItemClickListener;

    private ListScorePopup(Activity context) {
        super(context);
    }

    public ListScorePopup(Activity context, ListScorePopup.Builder builder) {
        this(context);
        mListView = (ListView) findViewById(R.id.popup_list);
        setAdapter(context, builder);
    }
    public static class Builder {
        private List<Score> mItemEventList = new ArrayList<>();
        private Activity mContext;

        public Builder(Activity context) {
            mContext = context;
        }

        public ListScorePopup.Builder addItem(Score itemTx) {
            mItemEventList.add(itemTx);
            return this;
        }

/*        public Builder addItem(int clickTag, Exam itemTx) {
            mItemEventList.add(new clickItemEvent(clickTag, itemTx));
            return this;
        }*/

        public List<Score> getItemEventList() {
            return mItemEventList;
        }

        public ListScorePopup build() {
            return new ListScorePopup(mContext, this);
        }

    }

    public static class clickItemEvent {
        private int clickTag;
        private Score itemTx;

        public clickItemEvent(int clickTag, Score itemTx) {
            this.clickTag = clickTag;
            this.itemTx = itemTx;
        }

        public int getClickTag() {
            return clickTag;
        }

        public void setClickTag(int clickTag) {
            this.clickTag = clickTag;
        }

        public Score getItemTx() {
            return itemTx;
        }

        public void setItemTx(Score itemTx) {
            this.itemTx = itemTx;
        }
    }

    //=============================================================adapter
    class ListPopupAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<Score> mItemList;

        public ListPopupAdapter(Context context, @NonNull List<Score> itemList) {
            mContext = context;
            mItemList = itemList;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mItemList.size();
        }

        @Override
        public Score getItem(int position) {
            if (mItemList.get(position) instanceof Score) {
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
            ListScorePopup.ListPopupAdapter.ViewHolder vh = null;
            if (convertView == null) {
                vh = new ListScorePopup.ListPopupAdapter.ViewHolder();
                convertView = mInflater.inflate(R.layout.item_score, parent, false);
                vh.tv_cname = (TextView) convertView.findViewById(R.id.textView7);
                vh.tv_cscore = (TextView) convertView.findViewById(R.id.textView8);
                vh.tv_cre = (TextView) convertView.findViewById(R.id.textView9);
                convertView.setTag(vh);
            } else {
                vh = (ListScorePopup.ListPopupAdapter.ViewHolder) convertView.getTag();
            }
            vh.tv_cname.setText(getItem(position).getcName());
            vh.tv_cscore.setText(getItem(position).getScore());
            vh.tv_cre.setText(getItem(position).getCredit());
            return convertView;
        }

        public List<Score> getItemList() {
            return this.mItemList;
        }


        class ViewHolder {
            public TextView tv_cname;
            public TextView tv_cscore;
            public TextView tv_cre;
        }
    }

    //=============================================================init
    private void setAdapter(Activity context, ListScorePopup.Builder builder) {
        if (builder.getItemEventList() == null || builder.getItemEventList().size() == 0) return;
        final ListScorePopup.ListPopupAdapter adapter = new ListScorePopup.ListPopupAdapter(context, builder.getItemEventList());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnListPopupItemClickListener != null) {
                    Object clickObj = adapter.getItemList().get(position);
                    if (clickObj instanceof String) {
                        mOnListPopupItemClickListener.onItemClick(position);
                    }
                    if (clickObj instanceof ListScorePopup.clickItemEvent) {
                        int what = ((ListScorePopup.clickItemEvent) clickObj).clickTag;
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
        return createPopupById(R.layout.popup_list_score);
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

    public ListScorePopup.OnListPopupItemClickListener getOnListPopupItemClickListener() {
        return mOnListPopupItemClickListener;
    }

    public void setOnListPopupItemClickListener(ListScorePopup.OnListPopupItemClickListener onListPopupItemClickListener) {
        mOnListPopupItemClickListener = onListPopupItemClickListener;
    }

    public interface OnListPopupItemClickListener {
        void onItemClick(int what);
    }
}
