package com.imeng.mycustomcontentloadingdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @Author : IMeng
 * @Date : 2017/8/25 16:30
 * @Version:
 */
public class ContentLoadingDialog extends ProgressDialog{
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
     * 显示，隐藏控制Handler
     */
    Handler mHandler = new Handler();


    public ContentLoadingDialog(Context context) {
        super(context);
    }

    public ContentLoadingDialog(Context context, int theme) {
        super(context, theme);
    }
    /**
     * copy from android.support.v4.widget.ContentLoadingProgressBar;
     */
    private final Runnable mDelayedHide = new Runnable() {
        @Override
        public void run() {
            mPostedHide = false;
            mStartTime = -1;
            ContentLoadingDialog.this.dismiss();
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
                ContentLoadingDialog.this.show();
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
            ContentLoadingDialog.this.dismiss();
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


    /**
     * Builder , 参考的AlterBuilder。
     */
    public static class Builder {
        private Context mContext;
        private String mMessage = "";
        private boolean mCancelable = true;

        public Builder(@NonNull Context context) {
            this.mContext = context;
        }

        @NonNull
        public Context getContext() {
            return this.mContext;
        }

        public Builder setMessage(@Nullable CharSequence message) {
            mMessage = message.toString();
            return this;
        }

        public Builder setCancelable(boolean b) {
            mCancelable = b;
            return this;
        }

        public ContentLoadingDialog create() {
            // We can't use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            final ContentLoadingDialog loadingDialog = new ContentLoadingDialog(mContext, R.style.ContentLoadDialogStyle);
            loadingDialog.setMessage(mMessage);
            loadingDialog.setCancelable(mCancelable);

            return loadingDialog;
        }

    }


}
