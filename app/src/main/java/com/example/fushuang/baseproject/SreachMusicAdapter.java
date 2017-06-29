package com.example.fushuang.baseproject;

import android.content.Context;
import android.widget.TextView;

import com.example.fushuang.baseproject.adapter.ZhyBaseRecycleAdapter.CommonAdapter;
import com.example.fushuang.baseproject.adapter.ZhyBaseRecycleAdapter.base.ViewHolder;

import java.util.List;

/**
 * Created by admin on 2017/6/18.
 */

public class SreachMusicAdapter extends CommonAdapter<MusicSearchResult.ShowapiResBodyBean.PagebeanBean.ContentlistBean> {

    public SreachMusicAdapter(Context context, int layoutId, List<MusicSearchResult.ShowapiResBodyBean.PagebeanBean.ContentlistBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MusicSearchResult.ShowapiResBodyBean.PagebeanBean.ContentlistBean contentlistBean, int position) {
        ((TextView) holder.getView(R.id.tv_songName)).setText(contentlistBean.getSongname()+"     "+contentlistBean.getSingername());
    }

}
