package com.sample.edwardtseng.mediacodecplayer;

import android.view.Surface;

/**
 * Created by edwardtseng on 2015/12/14.
 */
public interface PlayerInterface{
    void setDataSource(String filename, Surface surface);
    void play(long timeoutUs);
    void pause();
    void stop();
    void seek();
}
