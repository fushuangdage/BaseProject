package com.example.fushuang.baseproject.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.fushuang.baseproject.R;
import com.example.fushuang.baseproject.utils.AppManager;
import com.example.fushuang.baseproject.utils.dialog.LoadingDialog;
import com.example.fushuang.baseproject.utils.system.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity extends AppCompatActivity {
    public Unbinder mBind;
    public Toolbar mToolbar;
    public View mRootView;
    public Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        //注册EventBus
        EventBus.getDefault().register(this);
        //取消ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置界面随键盘弹出自动上移
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mRootView = LayoutInflater.from(this).inflate(setLayout(), null);
        //设置布局
        setContentView(mRootView);
        //绑定ButterKnife
        mBind = ButterKnife.bind(this);
        //添加Activity到管理堆栈
        AppManager.getInstance().addActivity(this);
        //设置UI
        initEvent();
        //初始化数据
        initData();
    }

    /**
     * 设置布局
     *
     * @return 布局资源id
     */
    public abstract
    @LayoutRes
    int setLayout();

    /**
     * 设置监听
     */
    public abstract void initEvent();

    /**
     * 初始化数据
     */
    public abstract void initData();


    /**
     * 获得根布局
     *
     * @return
     */
    public View getRootView() {
        return mRootView;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();//解除ButterKnife绑定
        EventBus.getDefault().unregister(this);//解除EventBus注册
        //从管理堆栈移除Activity
        AppManager.getInstance().finishActivity(this);
    }

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     *
     * @param view
     */
    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(view, 0);
    }

    /**
     * 获得StatusBar的高度
     *
     * @return StatusBar的高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置背景透明,设置透明后，系统默认不会让出状态栏的位置，布局延伸到状态栏位置
     *
     * @param on 是否透明
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        //透明状态栏
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void startActivity(Class clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public void startActivityForResult(Class clz, int requestCode) {
        Intent intent = new Intent(this, clz);
        startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Class clz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clz);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected void setToolbarIndicator(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(resId);
        }
    }

    protected void setToolbarTitle(String str) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(str);
        }
    }

    protected void setToolbarTitle(int strId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(strId);
        }
    }

    protected ActionBar getToolbar() {
        return getSupportActionBar();
    }

    protected View getDecorView() {
        return getWindow().getDecorView();
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


    /**
     * 显示进度框
     */
    public void showProgressDialog() {
        LoadingDialog.getInstance().show(this, "加载中...", false);
    }

    /**
     * 隐藏进度框
     */
    public void dismissProgressDialog() {
        LoadingDialog.getInstance().dismissDialog();
    }

    @Override
    public void onBackPressed() {
        dismissProgressDialog();
        finish();
    }

    /**
     * 设置状态栏颜色
     *
     * @param color 颜色
     */
    public void setStatusBarColor(int color) {
        setTranslucentStatus(true);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(color);//状态栏所需颜色
    }

    /**
     * 设置全屏，并且不会Activity的布局让出状态栏的空间
     */
    public void fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置全屏，并且不会Activity的布局让出状态栏的空间
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * Toast
     *
     * @param message 消息
     */
    public void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }
}
