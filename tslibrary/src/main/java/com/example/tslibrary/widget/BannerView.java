package com.example.tslibrary.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.tslibrary.util.ImageLoaderUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * banner无限循环滑动+自动播放
 * Created by zxg on 2016/11/9.
 * QQ:1092885570
 */

public class BannerView extends RelativeLayout {

    private static String TAG = BannerView.class.getSimpleName();

    private int mPosition = 1;
    private Context mContext;
    public ViewPager vp_banner;
    private LinearLayout ll_indicator;
    private Onclick onclick;

    //Banner展示的view
    private List<View> vp_views = new ArrayList<>();
    private List<ImageView> dot_imgs = new ArrayList<ImageView>();
    //指示器图片资源
    private int[] indicatorImgRes;

    private int mNextItem = 2; //当前banner的下一位置
    private int mPrevious = 0; //当前banner的上一位置
    private Timer mTimer;
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //实现循环自动播放的效果
            /*if (mNextItem == vp_views.size()){
                mNextItem = 0;
            }
            vp_banner.setCurrentItem(mNextItem);*/

            vp_banner.setCurrentItem(mPosition + 1);
        }
    };

    private int startTouchX; //保存touch按下的初始位置
    private int DISTANCE_X = 100; //判断滑动的最小滑动距离

    public BannerView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public void onBannerClick(Onclick onclick) {
        this.onclick = onclick;
    }

    private void initView() {
        vp_banner = new ViewPager(mContext);
        LinearLayout.LayoutParams vp_param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        vp_banner.setLayoutParams(vp_param);

        ll_indicator = new LinearLayout(mContext);
        LayoutParams dot_param = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        dot_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dot_param.setMargins(0, 0, 0, 10);
        ll_indicator.setLayoutParams(dot_param);
        ll_indicator.setGravity(Gravity.CENTER_HORIZONTAL);

        this.addView(vp_banner);
        this.addView(ll_indicator);
    }

    private View getBannerView(String path, float f) {
        ImageView imageView = new ImageView(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setElevation(3f);
            imageView.setTranslationZ(5f);
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageLoaderUtil.loadImage(mContext, path, imageView, f);
        return imageView;
    }

    /**
     * 设置banner展示的view和指示器图片
     *
     * @param paths        img的路径
     * @param indicatorRes
     * @param f
     * @param h
     */
    public void setView(List<String> paths, int[] indicatorRes, float f, int h) {
        if (paths == null || paths.size() == 0)
            return;

        vp_views.clear();

        //banner 循环关键
        vp_views.add(getBannerView(paths.get(paths.size() - 1), f));

        for (int i = 0; i < paths.size(); i++) {
            vp_views.add(getBannerView(paths.get(i), f));
        }

        //banner 循环关键
        vp_views.add(getBannerView(paths.get(0), f));

        vp_banner.setAdapter(new ViewPagerAdapter(vp_views));

        vp_banner.setCurrentItem(1);
        vp_banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //指示器跳转
                mPosition = position;
                int pageIndex = mPosition;
                if (mPosition == 0) {
                    pageIndex = dot_imgs.size();
                } else if (mPosition == dot_imgs.size() + 1) {
                    pageIndex = 1;
                }
                setIndicator(pageIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //ViewPager跳转
                int pageIndex = mPosition;
                if (mPosition == 0) {
                    pageIndex = vp_views.size() - 2;
                } else if (mPosition == vp_views.size() - 1) {
                    pageIndex = 1;
                }
                if (pageIndex != mPosition) {
                    vp_banner.setCurrentItem(pageIndex, false);
                }
            }
        });

        //加载指示器
        dot_imgs.clear();
        ll_indicator.removeAllViews();
        if (indicatorRes != null && indicatorRes.length >= 2) {
            indicatorImgRes = indicatorRes;
            for (int i = 0; i < vp_views.size() - 2; i++) {
                ImageView imageView = new ImageView(mContext);
                if (i == 0) {
                    imageView.setBackgroundResource(indicatorImgRes[0]);
                } else {
                    imageView.setBackgroundResource(indicatorImgRes[1]);
                }
                dot_imgs.add(imageView);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
//                int margin_h = (int) (0.016 * mContext.getResources().getDisplayMetrics().widthPixels);
//                int margin_v = (int) (0.02 * mContext.getResources().getDisplayMetrics().widthPixels);
                int margin_h = 3;
                int margin_v = h;
                lp.setMargins(margin_h, margin_v, margin_h, margin_v);
                ll_indicator.addView(imageView, lp);
            }
        }
    }

    public void setView(List<String> paths) {
        setView(paths, null, 0f, 0);
    }

    public void setView(List<String> paths, int[] indicatorRes) {
        setView(paths, indicatorRes, 10f, 10);
    }

    public void setView(List<String> paths, int[] indicatorRes, float f) {
        setView(paths, indicatorRes, f, 10);
    }

    /**
     * 设置ViewPager的图片
     *
     * @param img_src ViewPager图片资源
     */
    private void setViewPagerImg(int[] img_src) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < img_src.length + 2; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (i == 0) {
                imageView.setImageBitmap(readBitmap(mContext, img_src[img_src.length - 1]));
            } else if (i == img_src.length + 1) {
                imageView.setImageBitmap(readBitmap(mContext, img_src[0]));
            } else {
                imageView.setImageBitmap(readBitmap(mContext, img_src[i - 1]));
            }
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "bannerview onclick listener");
                }
            });
            vp_views.add(imageView);
        }
    }

    /**
     * 设置指示器的图片
     *
     * @param position 当前banner位置
     */
    private void setIndicator(int position) {
        for (int i = 0; i < dot_imgs.size(); i++) {
            if (position == i + 1) {
                dot_imgs.get(i).setBackgroundResource(indicatorImgRes[0]);
            } else {
                dot_imgs.get(i).setBackgroundResource(indicatorImgRes[1]);
            }
        }
    }

    /**
     * 开启自动轮播
     *
     * @param period banner轮播的周期
     */
    public void startAutoPlay(long period) {

        stopAutoPlay();

        //banner的vp_views数量大于1时，才允许自动轮播
        if (vp_views.size() > 1) {
            mTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandle.sendEmptyMessage(1);
                }
            };
            mTimer.schedule(timerTask, period, period);
        }
    }

    /**
     * 关闭自动轮播
     */
    public void stopAutoPlay() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    /**
     * 以最小内存读取本地资源图片
     *
     * @param context
     * @param bitmapResId
     * @return
     */
    public static Bitmap readBitmap(Context context, int bitmapResId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(bitmapResId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * ViewPager适配器
     * Created by zxg on 2016/11/8.
     * QQ:1092885570
     */

    class ViewPagerAdapter extends PagerAdapter {

        private List<View> vp_views;

        public ViewPagerAdapter(List<View> views) {
            this.vp_views = views;
        }

        @Override
        public int getCount() {
            return vp_views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = vp_views.get(position);
            if (onclick != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onclick.onBannerClick(position - 1);
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                container.removeView(vp_views.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public interface Onclick {
        void onBannerClick(int position);
    }
}
