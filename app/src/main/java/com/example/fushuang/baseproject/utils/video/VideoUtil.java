package com.example.fushuang.baseproject.utils.video;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

/**
 * Created by Administrator on 2016/11/3.
 */
public class VideoUtil {

    /**
     * 获取指定视频的某一帧
     * @param filePath
     * @param frameTime
     * @return
     */
    public static Bitmap getBitmapFromVideo(String filePath, int frameTime){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(filePath);
        Bitmap bitmap = mmr.getFrameAtTime(frameTime);
        mmr.release();
        return bitmap;
    }
}
