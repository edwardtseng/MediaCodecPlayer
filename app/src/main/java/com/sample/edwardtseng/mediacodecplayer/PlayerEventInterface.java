package com.sample.edwardtseng.mediacodecplayer;

/**
 * Created by edwardtseng on 2015/12/14.
 */
public interface PlayerEventInterface {
    void onStart();
    void onPlay();
    void onStop();
    int onError();
    void onPlayerUpdate();
}
