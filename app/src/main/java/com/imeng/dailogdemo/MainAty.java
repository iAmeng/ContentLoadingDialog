package com.imeng.dailogdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.imeng.mycustomcontentloadingdialog.MyLoadingDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Author : IMeng
 * @Date : 2017/8/23 10:25
 * @Version:
 */
public class MainAty extends AppCompatActivity {

    private MyLoadingDialog mLoadingDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom_loadingdialog_show_bt, R.id.custom_loadingdialog_hide_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.custom_loadingdialog_show_bt:
                showCustomLoadingDialog();
                break;
            case R.id.custom_loadingdialog_hide_bt:
                hideCustomLoadingDialog();
                break;
            default:
                break;
        }
    }

    private void hideCustomLoadingDialog() {
        if (mLoadingDialog != null){
            mLoadingDialog.hideLoadingDialog();
        }
    }

    private void showCustomLoadingDialog() {
        if (mLoadingDialog == null) {
            MyLoadingDialog.Builder builder = new MyLoadingDialog.Builder(this);
            mLoadingDialog = builder.setCancelable(true).setAlpha(0.99f).setTitle("CContentLoadingDialog").create();
        }
        mLoadingDialog.showLoadingDialog();
    }



}

