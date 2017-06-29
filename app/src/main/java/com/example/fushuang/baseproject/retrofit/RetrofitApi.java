package com.example.fushuang.baseproject.retrofit;

import com.example.fushuang.baseproject.LyricResult;
import com.example.fushuang.baseproject.MusicSearchResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Serenade on 17/6/10.
 */

public interface RetrofitApi {
    String Host="http://route.showapi.com/213-1/";
    String LyricHost="http://route.showapi.com/213-2/";


    @GET(Host)
    Call<MusicSearchResult> getSearchResult( @Query("keyword")String songName, @Query("page") int page);


    @GET(LyricHost)
    Call<LyricResult> getLyricResult(@Query("musicid") int musicid);


    @Streaming
    @GET()
    Call<ResponseBody> downLoadMusic(@Url String url);
}
