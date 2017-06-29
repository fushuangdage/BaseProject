package com.example.fushuang.baseproject.mvpbase;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * Created by hjn on 2016/11/3.
 */
public abstract class MVPBasePresenter<T extends IBaseView> {
    protected T mView;
    protected List<Call> mRequests = new ArrayList<>();

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detach() {
        if (mView != null) {
            mView = null;
            //取消该页面所有网络请求
            cancelRequest();
        }
    }

    public boolean isAttach() {
        return mView != null;
    }

    /**
     * 取消该页面所有网络请求
     */
    public void cancelRequest() {
        for (Call request : mRequests) {
            request.cancel();
            mRequests.remove(request);
        }
    }

    /**
     * 记录该页面的所有网络请求
     * @param call
     */
    public void recordRequest(Call call){
        mRequests.add(call);
    }
}