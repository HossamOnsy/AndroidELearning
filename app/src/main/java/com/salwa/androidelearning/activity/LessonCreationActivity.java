package com.salwa.androidelearning.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.salwa.androidelearning.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LessonCreationActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageRef;
    SimpleExoPlayer player;
    @BindView(R.id.sepv)
    SimpleExoPlayerView sepv;
    @BindView(R.id.upload)
    Button upload;
    @BindView(R.id.successful_txt)
    TextView successfulTxt;
    @BindView(R.id.container)
    LinearLayout container;
    StorageReference ref;


    private static int ACTIVITY_CHOOSE_FILE = 1001;
    private static int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_creation);
        ButterKnife.bind(this);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        ref = storageRef.child("videos");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.upload)
    public void onViewClicked() {
        if (checkSelfPermission( android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
            //File write logic here
            requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        } else {
            uploadFile();
        }
    }

    void uploadFile() {
//        Intent chooseFile;
//        Intent intent;
//
//        String[] mimeTypes = { "text/plain"};
//        Intent intent = new Intent()
//                .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
//                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//                .setType("file/*")
//                //.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                .setAction(Intent.ACTION_GET_CONTENT);
//
//        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
//        chooseFile.setType("file/*");
//        intent = Intent.createChooser(chooseFile, "Choose a file");
//        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);

        String[] mimeTypes = {"*/*"};
        Intent intent = new Intent()
                .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                .setType("file/*")
                //.setAction(Intent.ACTION_OPEN_DOCUMENT);
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), ACTIVITY_CHOOSE_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        successfulTxt.setVisibility(View.GONE);
        if (resultCode != RESULT_OK) return;
        String path = "";
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            Log.v("data", "   ->   " + data.getData());


            Uri uri = data.getData();
            //-----------------------------------------------------------
            String uriString = uri.toString();

            File myFile = new File(uriString);

            path = myFile.getAbsolutePath();

            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                        final String finalDisplayName = displayName;
                        File dir = Environment.getExternalStorageDirectory();
                        File yourFile = new File(dir, finalDisplayName);

                        path = yourFile.getPath();
                        Toast.makeText(this, "hiiiii" + yourFile, Toast.LENGTH_SHORT).show();
                        //Toast.makeText ( getActivity (),"hii"+displayName,Toast.LENGTH_LONG ).show ();
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
//                path = myFile.get();
                // Toast.makeText ( getActivity (),"hii"+displayName,Toast.LENGTH_LONG ).show ();
            }
            //-----------------------------------------------------------
//            File file = new File(uri.getPath());//create path from uri
//            final String[] split = file.getPath().split(":");//split the path.
//            path= split[1];//assign it to a string(your choice).
//            path = getImageFilePath(uri);
//            Uri uri = data.getData();
//            File file = new File(uri.getPath());
//             path = file.getAbsolutePath();
//            String FilePath = getRealPathFromURI(uri); // should the path be here in this string
//            System.out.print("Path  = " + FilePath);

            Uri uritest = Uri.fromFile(new File(path));

            UploadTask uploadTask = ref.putFile(uritest);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    String Uri = taskSnapshot.getMetadata().getPath();
                    successfulTxt.setVisibility(View.VISIBLE);
                    addLessonToDatabse(Uri);


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("Upload is " + progress + "% done");
                }
            });
        }
    }

    private void addLessonToDatabse(String path) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Lessons");
        mDatabase.push().setValue(path);

        try {
            Log.v("DataNotRecieved", "DataNotRecieved --- IF of Re ------> " + path);
            Uri uri = Uri.parse(path);
            Log.v("DataNotRecieved", "DataNotRecieved --- IF of Re ------> " + uri);
            initializePlayer(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            player.prepare(mediaSource, true, false);
            player.setVideoDebugListener(new VideoRendererEventListener() {
                @Override
                public void onVideoEnabled(DecoderCounters counters) {
//                    progress_bar.setVisibility(View.GONE);
                }

                @Override
                public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

                }

                @Override
                public void onVideoInputFormatChanged(Format format) {

                }

                @Override
                public void onDroppedFrames(int count, long elapsedMs) {

                }

                @Override
                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

                }

                @Override
                public void onRenderedFirstFrame(Surface surface) {

                }

                @Override
                public void onVideoDisabled(DecoderCounters counters) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            uploadFile();
            //resume tasks needing this permission
        } else {
            Toast.makeText(this, "Please you need to allow permission to choose your video to upload it "
                    , Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
//    public String getRealPathFromURI(Context context, Uri contentUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = { MediaStore.Images.Media.DATA };
//            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }

    public String getImageFilePath(Uri uri) {
        String path = null, image_id = null;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            image_id = cursor.getString(0);
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
            cursor.close();
        }

//         cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
//        if (cursor!=null) {
//            cursor.moveToFirst();
//            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//            cursor.close();
//        }
        return path;
    }


}
