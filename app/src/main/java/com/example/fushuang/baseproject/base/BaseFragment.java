package com.example.fushuang.baseproject.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.fushuang.baseproject.utils.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by admin on 2016/11/1.
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;
    protected Activity mActivity;
    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(setLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        EventBus.getDefault().register(this);
        mActivity = getActivity();
        return mRootView;
    }

    public View getRootView() {
        return mRootView;
    }

    protected abstract int setLayout();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden)
            onPause();
        else
            onResume();
    }

    @Subscribe
    public void onReceiveEvent(EventCenter event) {
        onEventBusResult(event);
    }

    /**
     * EventBus回传消息重写方法
     *
     * @param event
     */
    public void onEventBusResult(EventCenter event) {

    }

    @Override
    public void onDestroy() {
        mActivity = null;
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    protected void showLoadingDialog() {
        LoadingDialog.getInstance().show(mActivity, "加载中...", true);
    }

    protected void dismissDialog() {
        LoadingDialog.getInstance().dismissDialog();
    }

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     *
     * @param view
     */
    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(view, 0);
    }
}
