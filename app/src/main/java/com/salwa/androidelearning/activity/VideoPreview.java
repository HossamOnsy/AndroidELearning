package com.salwa.androidelearning.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salwa.androidelearning.CustomAdapter;
import com.salwa.androidelearning.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPreview extends AppCompatActivity {
    String uri;


    SimpleExoPlayer player;
    @BindView(R.id.sepv)
    SimpleExoPlayerView sepv;

    String type_Of_Actvitiy="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        ButterKnife.bind(this);

        if(getIntent().hasExtra("type_Of_Actvitiy")){
            type_Of_Actvitiy  = getIntent().getStringExtra("type_Of_Actvitiy");
        }

        if (getIntent().hasExtra("uri")) {
            uri = getIntent().getStringExtra("uri");
            initializePlayer(Uri.parse(uri));
        }
//        uri

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            player.stop();
            player.release();
        }catch (Exception e){
            e.printStackTrace();
        }

        DatabaseReference ref1;
        DatabaseReference ref2;


        ref1 = FirebaseDatabase.getInstance().getReference();
        ref2 = ref1.child("Progress");
        ref2.child(FirebaseAuth.getInstance().getUid()).push().setValue(type_Of_Actvitiy);


    }

    private void initializePlayer(Uri uri) {
        if (uri != null) {
//            SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(
//                    new DefaultRenderersFactory(getActivity().getApplicationContext()),
//                    new DefaultTrackSelector(), new DefaultLoadControl());

            MediaSource mediaSource = buildMediaSource(uri);
            if (player == null) {
                player = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getApplicationContext()),
                        new DefaultTrackSelector(), new DefaultLoadControl());

            }
//            if(bundle!=null)
//                isPlayWhenReady = bundle.getBoolean("playstate");
            player.setPlayWhenReady(true);
            player.prepare(mediaSource, false, false);
            player.setVideoDebugListener(new VideoRendererEventListener() {
                @Override
                public void onVideoEnabled(DecoderCounters counters) {
//                    progress_bar.setVisibility(View.GONE);
                    Log.v("TestingPlayer", "onVideoEnabled is granted");
                }

                @Override
                public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

                    Log.v("TestingPlayer", "onVideoDecoderInitialized is granted");
                }

                @Override
                public void onVideoInputFormatChanged(Format format) {
                    Log.v("TestingPlayer", "onVideoInputFormatChanged is granted");

                }

                @Override
                public void onDroppedFrames(int count, long elapsedMs) {
                    Log.v("TestingPlayer", "onDroppedFrames is granted");

                }

                @Override
                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

                    Log.v("TestingPlayer", "onVideoSizeChanged is granted");
                }

                @Override
                public void onRenderedFirstFrame(Surface surface) {

                    Log.v("TestingPlayer", "onRenderedFirstFrame is granted");
                }

                @Override
                public void onVideoDisabled(DecoderCounters counters) {

                    Log.v("TestingPlayer", "onVideoDisabled is granted");
                }
            });

            sepv.setPlayer(player);


        }

//        player.setPlayWhenReady(playWhenReady);
//        player.seekTo(currentWindow, playbackPosition);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }



}
