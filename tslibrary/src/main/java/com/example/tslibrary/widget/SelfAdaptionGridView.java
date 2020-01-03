package com.example.tslibrary.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tslibrary.R;
import com.example.tslibrary.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： HeroCat
 * 时间：2020/1/3/003
 * 描述：横向自适应网格布局
 */
public class SelfAdaptionGridView extends LinearLayout {

    private Context mContext;
    private int widthAdd;
    private LinearLayout li;

    public SelfAdaptionGridView(Context context) {
        super(context, null);
    }

    public SelfAdaptionGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        this.mContext = context;
    }

    public void setDatas(final List<String> datas, final CheckListener listener) {


        DisplayMetrics dm = getResources().getDisplayMetrics();
        int W = dm.widthPixels - DensityUtil.dip2px(mContext, 32);

        final List<String> checkDatas = new ArrayList<>();

        if (datas == null || datas.size() == 0)
            return;

        for (int i = 0; i < datas.size(); i++) {

            if (li == null)
                li = newLayout();

            final String info = datas.get(i);
            final CheckBox textView = new CheckBox(mContext);
            LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(mContext, 25));
            textView.setPadding(DensityUtil.dip2px(mContext, 12), DensityUtil.dip2px(mContext, 3), DensityUtil.dip2px(mContext, 12), DensityUtil.dip2px(mContext, 3));
            textView.setBackgroundResource(R.drawable.radion_bg);
            textView.setButtonDrawable(null);
            textView.setLayoutParams(lp);
            textView.setTextColor(mContext.getResources().getColor(R.color.color_lib_black));
            textView.setText(info);
            textView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkDatas.add(info);
                        textView.setTextColor(mContext.getResources().getColor(R.color.color_lib_white));
                    } else {
                        checkDatas.remove(info);
                        textView.setTextColor(mContext.getResources().getColor(R.color.color_lib_black));
                    }

                    if (listener != null)
                        listener.checkDatas(checkDatas);

                }
            });

            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int popupWidth = textView.getMeasuredWidth();

            if (widthAdd + popupWidth + DensityUtil.dip2px(mContext, 10) > W) {
                addView(li);
                li = newLayout();
                widthAdd = popupWidth;
            }

            if (li.getChildCount() == 0) {
                widthAdd = popupWidth;
            } else {
                lp.setMargins(DensityUtil.dip2px(mContext, 10), 0, 0, 0);
                textView.setLayoutParams(lp);
                widthAdd = widthAdd + popupWidth + DensityUtil.dip2px(mContext, 10);
            }

            li.addView(textView);

            if (i == datas.size() - 1)
                addView(li);

        }

    }

    private LinearLayout newLayout() {
        LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, DensityUtil.dip2px(mContext, 25));
        lp1.setMargins(0, DensityUtil.dip2px(mContext, 10), 0, 0);
        LinearLayout li = new LinearLayout(mContext);
        li.setLayoutParams(lp1);
        li.setGravity(HORIZONTAL);
        return li;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface CheckListener {
        void checkDatas(List<String> datas);
    }

}
