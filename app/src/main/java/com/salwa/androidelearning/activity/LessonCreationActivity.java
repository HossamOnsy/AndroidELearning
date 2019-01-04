package com.salwa.androidelearning.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.salwa.androidelearning.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    String databasePath = "Lessons";


    private static int ACTIVITY_CHOOSE_FILE = 1001;
    private static int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_creation);
        ButterKnife.bind(this);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        if(getIntent().hasExtra("databasePath")){
            databasePath = getIntent().getStringExtra("databasePath");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.upload)
    public void onViewClicked() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
            //File write logic here
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

        } else {
            uploadFile();
        }
    }

    void uploadFile() {


        String[] mimeTypes = {"*/*"};
        Intent intent = new Intent()
                .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                .setType("file/*")
                //.setAction(Intent.ACTION_OPEN_DOCUMENT);
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), ACTIVITY_CHOOSE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();

                    {
                        String filename;
                        String mimeType = getContentResolver().getType(uri);
                        if (mimeType == null) {
                            String path = getPath(this, uri);
                            if (path == null) {
                                filename = uri.toString();
                            } else {
                                File file = new File(path);
                                filename = file.getName();
                            }
                        } else {
                            Uri returnUri = data.getData();
                            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                            returnCursor.moveToFirst();
                            filename = returnCursor.getString(nameIndex);
                            String size = Long.toString(returnCursor.getLong(sizeIndex));
                        }
                        File fileSave = getExternalFilesDir(null);
                        String sourcePath = getExternalFilesDir(null).toString();


                        Log.v("sourcePathsourcePath", "sourcePath + \"/\" + filename -->  " + sourcePath + "/" + filename);
                        try {
                            copyFileStream(new File(sourcePath + "/" + filename), uri, this);

                            Uri uriTesting = Uri.fromFile(new File(sourcePath + "/" + filename));
//                            initializePlayer(uriTesting);

                            ref = storageRef.child("videos").child(filename);
                            UploadTask uploadTask = ref.putFile(uriTesting);

// Register observers to listen for when the download is done or if it fails
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    Log.v("sourcePathsourcePath", "onFailure" + exception.getMessage());
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                    Log.v("sourcePathsourcePath", "onFailure" + taskSnapshot.toString());
//                                    taskSnapshot.
//                                    Uri downloadUri = taskSnapshot.getMetadata().get();
                                    String Uri = ref.getDownloadUrl().toString();
                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;

                                    Uri downloadUrl = urlTask.getResult();

                                    successfulTxt.setVisibility(View.VISIBLE);
//                                    initializePlayer(uri);
                                    addLessonToDatabse(downloadUrl.toString());


                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    System.out.println("Upload is " + progress + "% done");
                                }
                            });

                            Log.v("sourcePathsourcePath", "sourcePath + \"/\" + filename -->  " + sourcePath + "/" + filename);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyFileStream(File dest, Uri uri, Context context)
            throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            is.close();
            os.close();
        }
    }

    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        successfulTxt.setVisibility(View.GONE);
//        if (resultCode != RESULT_OK) return;
//        String path = "";
//        if (requestCode == ACTIVITY_CHOOSE_FILE) {
//            Log.v("datadata", "   ->   " + data.getData());
//
//
//            Uri uri = data.getData();
//            //-----------------------------------------------------------
//            String uriString = uri.toString();
//
//            File myFile = new File(uriString);
//
//            path = getPath(this,uri);
//
//            String displayName = null;
//
////            if (uriString.startsWith("content://")) {
////                Cursor cursor = null;
////                try {
////                    cursor = this.getContentResolver().query(uri, null, null, null, null);
////                    if (cursor != null && cursor.moveToFirst()) {
////                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
////
////                        Log.v("DataNotRecieved", "DataNotRecieved --- IF of Re ------> " + displayName);
//////                        displayName.
////                        final String finalDisplayName = displayName;
////                        File dir = Environment.getExternalStorageDirectory();
////                        File yourFile = new File(dir, path+finalDisplayName);
////
////                        path = yourFile.getPath();
////
////                        Toast.makeText(this, "hiiiii" + yourFile, Toast.LENGTH_SHORT).show();
////                        //Toast.makeText ( getActivity (),"hii"+displayName,Toast.LENGTH_LONG ).show ();
////                    }
////                } finally {
////                    cursor.close();
////                }
////            } else if (uriString.startsWith("file://")) {
////                displayName = myFile.getName();
//////                path = myFile.get();
////                // Toast.makeText ( getActivity (),"hii"+displayName,Toast.LENGTH_LONG ).show ();
////            }
//            //-----------------------------------------------------------
////            File file = new File(uri.getPath());//create path from uri
////            final String[] split = file.getPath().split(":");//split the path.
////            path= split[1];//assign it to a string(your choice).
////            path = getImageFilePath(uri);
////            Uri uri = data.getData();
////            File file = new File(uri.getPath());
////             path = file.getAbsolutePath();
////            String FilePath = getRealPathFromURI(uri); // should the path be here in this string
////            System.out.print("Path  = " + FilePath);
//
////            Uri photoURI = FileProvider.getUriForFile(LessonCreationActivity.this,
////                    BuildConfig.APPLICATION_ID + ".provider",
////                    new File(path));
//
//            Uri uritest = Uri.fromFile(new File(path));
//
//
//
//            UploadTask uploadTask = ref.putFile(uritest);
//
//// Register observers to listen for when the download is done or if it fails
//            uploadTask.addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                }
//            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                    // ...
//                    String Uri = taskSnapshot.getMetadata().getPath();
//                    successfulTxt.setVisibility(View.VISIBLE);
//                    addLessonToDatabse(Uri);
//
//
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                    System.out.println("Upload is " + progress + "% done");
//                }
//            });
//        }
//    }
    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private void addLessonToDatabse(String path) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(databasePath);
        mDatabase.push().setValue(path);

        try {
            Uri uri = Uri.parse(path);
            initializePlayer(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
