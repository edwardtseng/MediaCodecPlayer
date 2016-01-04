package com.sample.edwardtseng.mediacodecplayer;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Button mPlayBtn;
    private Button mStopBtn;

    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private MediaPlayer mPlayer;
    boolean pausing = false;
    public static String filepath = "/storage/ext_sd/Pictures/temp/2015-05-13-193624-65.mp4";
    final String tags = this.tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getWindow().setFormat(PixelFormat.UNKNOWN);
        mPreview = (SurfaceView)findViewById(R.id.surfaceView);
        mPlayBtn = (Button) findViewById(R.id.button);
        mStopBtn = (Button) findViewById(R.id.button2);
        mPlayBtn.setX(100);
        mPlayBtn.setY(500);
        mStopBtn.setX(100);
        mStopBtn.setY(700);
        mHolder = mPreview.getHolder();
        mHolder.setFixedSize(800, 480);
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        mPlayBtn.setOnClickListener(new Button.OnClickListener() {

            @Override

            public void onClick(View v) {
                // TODO Auto-generated method stub
                play();
            }

        });
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop();
            }
        });
    }

 /*   protected void onPause(){
        if (mPlayer!=null && mPlayer.isPlaying()) {
            super.onPause();
            mPlayer.release();
        }
    }*/

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub

    }
    void play(){

        if(mPlayer!=null)
        {
            if  (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                Log.e(tags, "Cannot Play when Playing");
                return;
            }
        }
        else
        {
            if (mHolder!=null) {
                mPlayer = new MediaPlayer();
                mPlayer.setSurface(mHolder.getSurface());
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mPlayer.stop();
                        mPlayer.release();
                        mPlayer = null;
                    }
                });
            }
            else
            {
                Log.e(tags, "Cannot Play without Surface");
                return;
            }
        }


        try {
            //FileInputStream fileInputStream = new FileInputStream("storage/ext_sd/Pictures/temp/2015-05-13-193624-65.mp4");
            mPlayer.setDataSource("storage/ext_sd/Pictures/temp/2015-05-13-193624-65.mp4");//fileInputStream.getFD());

            mPlayer.prepareAsync();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void stop()
    {
        Log.d(tags, "Stop video!");
        if (mPlayer != null)
        {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
