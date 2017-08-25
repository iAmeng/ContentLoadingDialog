package com.imeng.mycustomcontentloadingdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * @Author : IMeng
 * @Date : 2017/8/24 10:29
 * @Version:
 */
public class MyLoadingDialog extends AppCompatDialog {

    private ContentLoadingProgressBar mContentLoadingProgressBar;
    private TextView mContentTv;
    private float mDialogHeight = 120.0f;
    private String mTitle;
    private float mAlpha;//loading框的透明度0-1f

    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    private static final int MIN_SHOW_TIME = 500; // ms
    private static final int MIN_DELAY = 500; // ms
    long mStartTime = -1;
    boolean mPostedHide = false;
    boolean mPostedShow = false;
    boolean mDismissed = false;


    /**
     * 加载ContentView的开关
     */
    private boolean firstShow = true;

    /**
     * 显示，隐藏控制Handler
     */
    Handler mHandler = new Handler();

    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    private final Runnable mDelayedHide = new Runnable() {
        @Override
        public void run() {
            mPostedHide = false;
            mStartTime = -1;
            MyLoadingDialog.this.hide();
        }
    };

    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    private final Runnable mDelayedShow = new Runnable() {
        @Override
        public void run() {
            mPostedShow = false;
            if (!mDismissed) {
                mStartTime = System.currentTimeMillis();
                MyLoadingDialog.this.showDialog();
            }
        }
    };
    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks();
    }
    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks();
    }
    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    private void removeCallbacks() {
        mHandler.removeCallbacks(mDelayedHide);
        mHandler.removeCallbacks(mDelayedShow);
    }
    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    public void hideLoadingDialog() {
        mDismissed = true;
        mHandler.removeCallbacks(mDelayedShow);

        mPostedShow = false; //add IMeng 2017-08-25

        long diff = System.currentTimeMillis() - mStartTime;
        if (diff >= MIN_SHOW_TIME || mStartTime == -1) {
            MyLoadingDialog.this.dismiss();
        } else {
            if (!mPostedHide) {
                mHandler.postDelayed(mDelayedHide, MIN_SHOW_TIME - diff);
                mPostedHide = true;
            }
        }
    }
    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    public void showLoadingDialog() {
        mStartTime = -1;
        mDismissed = false;
        mHandler.removeCallbacks(mDelayedHide);

        mPostedHide = false; //add IMeng 2017-08-25

        if (!mPostedShow) {
            mHandler.postDelayed(mDelayedShow, MIN_DELAY);
            mPostedShow = true;
        }
    }



    ///////////////////////////////////////////////////////////////////////////
    // My Code
    ///////////////////////////////////////////////////////////////////////////

    public MyLoadingDialog(@NonNull Context context) {
        super(context);
    }

    public MyLoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MyLoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    private void showDialog() {
        super.show();
        if (firstShow) {
            setTranslateValue();
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
            setMyContentView();
            setDialogWindowAttr(this, getContext());
            firstShow = false;
        } else {

        }
    }

    /**
     * 不要用这个方法
     */
    @Override
    public void dismiss() {
        super.dismiss();

    }

    private void setTranslateValue() {
        Window window = this.getWindow();
        //loading框透明度
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = this.mAlpha;
        window.setAttributes(lp);
    }

    private void setMyContentView() {
        View loading_dialog_ll = LayoutInflater.from(getContext()).inflate(R.layout.viwe_loading, null);
        mContentTv = (TextView) loading_dialog_ll.findViewById(R.id.content_tv);
        mContentTv.setText(mTitle);

        mContentLoadingProgressBar = (ContentLoadingProgressBar) loading_dialog_ll.findViewById(R.id.contentloadingpb);
        mContentLoadingProgressBar.getIndeterminateDrawable().setColorFilter(0xFF1E88E5, PorterDuff.Mode.MULTIPLY);
        setContentView(loading_dialog_ll, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    //在dialog.show()之后调用
    public void setDialogWindowAttr(Dialog dlg, Context ctx) {
        Window window = dlg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;//宽高可设置具体大小
        lp.height = dp2pxUtil(mDialogHeight);
        dlg.getWindow().setAttributes(lp);
    }


    public int dp2pxUtil(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getContext().getResources().getDisplayMetrics());
    }

    public void setTitle(String title) {
        mTitle = title;
    }
    public void setAlpha(float alpha) {
        mAlpha = alpha;
    }

    /**
     * Builder , 参考的AlterBuilder。
     */
    public static class Builder {
        private Context mContext;
        private String mTitle = "";
        private float mAlpha = 0.5f;
        private boolean mCancelable = true;

        public Builder(@NonNull Context context) {
            this.mContext = context;
        }

        @NonNull
        public Context getContext() {
            return this.mContext;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            mTitle = title.toString();
            return this;
        }

        public Builder setCancelable(boolean b) {
            mCancelable = b;
            return this;
        }

        public Builder setAlpha(float alpha) {
            mAlpha = alpha;
            return this;
        }

        public MyLoadingDialog create() {
            // We can't use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            final MyLoadingDialog loadingDialog = new MyLoadingDialog(mContext);
            loadingDialog.setTitle(mTitle);
            loadingDialog.setAlpha(mAlpha);
            loadingDialog.setCancelable(mCancelable);

            return loadingDialog;
        }

    }


}
