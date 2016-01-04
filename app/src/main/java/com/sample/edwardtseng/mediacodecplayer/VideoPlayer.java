package com.sample.edwardtseng.mediacodecplayer;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaCodec.BufferInfo;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;

/**
 * Created by edwardtseng on 2015/12/14.
 */
public class VideoPlayer implements PlayerInterface {
    MediaExtractor extractor;
    MediaCodec decoder;
    String fileadd;
    Surface locSurface;
    final String tags = this.tags;

    @Override
    public void setDataSource(String filename, Surface surface) {
        fileadd = filename;
        locSurface = surface;

        try {
            extractor = new MediaExtractor();
            extractor.setDataSource(fileadd);

            for (int i=0; i<extractor.getTrackCount(); i++)
            {
                MediaFormat format = extractor.getTrackFormat(i);
                String mine = format.getString(MediaFormat.KEY_MIME);
                if (mine.startsWith("video/"))
                {
                    extractor.selectTrack(i);
                    decoder = MediaCodec.createDecoderByType(mine);
                    decoder.configure(format, surface, null, 0);
                    break;
                }
            }

            if (decoder == null)
            {
                Log.d(tags, "No good information from extractor!");
            }
            else
            {
                Log.d(tags, "good to create codec!");
            }
            return;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void play(long timeoutUs) {

        Boolean isEos = false;
        ByteBuffer[] inputBuffers = decoder.getInputBuffers();
        ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
        BufferInfo info = new BufferInfo();

        if (decoder == null) {
            Log.d(tags, "No decoder is created!");
            return;
        }

        decoder.start();

        while(true) {
            if (!isEos) {
                int inIndex = decoder.dequeueInputBuffer(10000);
                if (inIndex >= 0) {
                    ByteBuffer buffer = inputBuffers[inIndex];
                    int sampleSize = extractor.readSampleData(buffer, 0);
                    if (sampleSize < 0) {
                        Log.d(tags, "BUFFER_FLAG_END_OF_STREAM");
                        decoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                        isEos = true;
                    } else {
                        decoder.queueInputBuffer(inIndex, 0, sampleSize, extractor.getSampleTime(), extractor.getSampleFlags());
                        extractor.advance();
                    }
                }
            }
            int outIndex = decoder.dequeueOutputBuffer(info, 10000);
            switch(outIndex)
            {
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                    Log.d(tags, "INFO_OUTPUT_BUFFERS_CHANGED");
                    outputBuffers = decoder.getOutputBuffers();
                    break;
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    Log.d(tags, "New format " + decoder.getOutputFormat());
                    break;
                case MediaCodec.INFO_TRY_AGAIN_LATER:
                    Log.d(tags, "dequeueOutputBuffer timed out!");
                    break;
                default:
                    ByteBuffer buffer = outputBuffers[outIndex];
                    Log.v(tags, "We can't use this buffer but render it due to the API limit, ");
                    decoder.releaseOutputBuffer(outIndex, true);
                    break;
            }

            if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                Log.d(tags, "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                break;
            }
            decoder.stop();
            decoder.release();
            extractor.release();
        }
    }

    @Override
    public void pause(){

    }

    @Override
    public void stop(){

    }

    @Override
    public void seek(){

    }
}
