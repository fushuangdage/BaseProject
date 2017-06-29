package com.example.fushuang.baseproject;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fushuang.baseproject.adapter.ZhyBaseRecycleAdapter.MultiItemTypeAdapter;
import com.example.fushuang.baseproject.base.BaseActivity;
import com.example.fushuang.baseproject.retrofit.RetrofitHelper;
import com.example.fushuang.baseproject.utils.SPUtil;
import com.example.fushuang.baseproject.utils.img.GlideRoundTransform;
import com.example.fushuang.baseproject.utils.string.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, MultiItemTypeAdapter.OnItemClickListener,MediaPlayer.OnPreparedListener {

    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    @BindView(R.id.recycleView)
    RecyclerView mRecyclerView;
    @BindView(R.id.bottom_bar_icon)
    ImageView bottom_bar_icon;
    @BindView(R.id.tv_songName)
    TextView tv_songname;
    @BindView(R.id.tv_singerName)
    TextView tv_singerName;


    private ImageView mCircleImageView;
    private Platform qq;
    private TextView mUName;
    private Handler mHandler=new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 1:
                    User obj = (User) msg.obj;
                    Glide.with(mActivity).load(obj.icon).transform(new GlideRoundTransform(mActivity)).into(mCircleImageView);
                    mUName.setText(obj.name);
                    break;
            }

        }
    };
    private List<MusicSearchResult.ShowapiResBodyBean.PagebeanBean.ContentlistBean> mList;
    private SreachMusicAdapter mAdapter;
    private MediaPlayer mMediaPlayer;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initEvent() {
        initToolbar();
        setTranslucentStatus(true);
        setStatusBarColor(Color.TRANSPARENT);
        qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(false);
        qq.setPlatformActionListener(this);
        mUName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tv_userName);
        mCircleImageView= (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.civ_icon);
        mUName.setOnClickListener(this);

        mList = new ArrayList<>();
        mAdapter = new SreachMusicAdapter(this, R.layout.item_search_music, mList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);
        String uname = (String) SPUtil.get(this, "uname", "");
        if (!StringUtil.isEmpty(uname)) {
            mUName.setText(uname);
        }
        String icon = (String) SPUtil.get(this, "icon", "");
        if (!StringUtil.isEmpty(icon)) {
            Glide.with(mActivity).load(icon).transform(new GlideRoundTransform(mActivity)).into(mCircleImageView);
        }


    }

    @Override
    public void initData() {
        startService(new Intent(this,PlayService.class));

        bindService(new Intent(this, PlayService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMediaPlayer = ((PlayService.PlayBinder) service).getMediaPlayer();
                mMediaPlayer.setOnPreparedListener(MainActivity.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Service.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sreach_main_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.ab_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RetrofitHelper.getApi().getSearchResult(query,1).enqueue(new Callback<MusicSearchResult>() {
                    @Override
                    public void onResponse(Call<MusicSearchResult> call, Response<MusicSearchResult> response) {
                        MusicSearchResult body = response.body();
                        mList.clear();
                        mList.addAll(body.getShowapi_res_body().getPagebean().getContentlist());
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<MusicSearchResult> call, Throwable t) {

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_userName:
                String s = qq.getDb().exportData();
                qq.showUser(null);
                break;
        }
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String s = platform.getDb().exportData();
        String nickname = (String) hashMap.get("nickname");
        User user = new User(nickname, (String) hashMap.get("figureurl_qq_1"));
        mHandler.sendMessage(mHandler.obtainMessage(1,user));
//        Glide.with(this).load(((String) hashMap.get("figureurl_qq_1"))).centerCrop().into(mCircleImageView);
        SPUtil.put(this,"uname", hashMap.get("nickname"));
        SPUtil.put(this,"icon", hashMap.get("figureurl_qq_1"));
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        MusicSearchResult.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean = mList.get(position);
        tv_singerName.setText(bean.getSingername());
        tv_songname.setText(bean.getSongname());
        Glide.with(this).load(bean.getAlbumpic_small()).into(bottom_bar_icon);
        try {
            if (mMediaPlayer.isPlaying()){
                mMediaPlayer.stop();
                mMediaPlayer.reset();
            }
            mMediaPlayer.setDataSource(bean.getM4a());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
