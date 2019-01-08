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
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.salwa.androidelearning.models.QuizModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizzesCreation extends AppCompatActivity {

    @BindView(R.id.guideline)
    Guideline guideline;
    @BindView(R.id.upload_question_btn)
    Button uploadQuestionBtn;
    @BindView(R.id.correctanswer)
    EditText correctanswer;
    @BindView(R.id.upload_answer_one_btn)
    Button uploadAnswerOneBtn;
    @BindView(R.id.upload_answer_two_btn)
    Button uploadAnswerTwoBtn;
    @BindView(R.id.upload_answer_three_btn)
    Button uploadAnswerThreeBtn;
    @BindView(R.id.upload_answer_four_btn)
    Button uploadAnswerFourBtn;
    @BindView(R.id.container)
    LinearLayout container;
    StorageReference ref;
    String databasePath = "Quizzes";

    FirebaseStorage storage;
    StorageReference storageRef;
    QuizModel quizModel;
    private static int REQUEST_CODE = 1111;
    private static int ACTIVITY_CHOOSE_FILE = 1001;
    private static int ACTIVITY_CHOOSE_ANSWERONE = 1002;
    private static int ACTIVITY_CHOOSE_ANSWERTWO = 1003;
    private static int ACTIVITY_CHOOSE_ANSWERTHREE = 1004;
    private static int ACTIVITY_CHOOSE_ANSWERFOUR = 1005;
    @BindView(R.id.upload_quiz)
    Button uploadQuiz;
    String path = "English";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quizzes_creation);
        ButterKnife.bind(this);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        quizModel = new QuizModel();

        if(getIntent().hasExtra("Subject")){
            path = getIntent().getStringExtra("Subject");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick({R.id.upload_quiz, R.id.upload_question_btn, R.id.upload_answer_one_btn, R.id.upload_answer_two_btn, R.id.upload_answer_three_btn, R.id.upload_answer_four_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.upload_question_btn:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
                    //File write logic here
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

                } else {
                    uploadFile(R.id.upload_question_btn);
                }
                break;
            case R.id.upload_answer_one_btn:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
                    //File write logic here
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

                } else {
                    uploadFile(R.id.upload_answer_one_btn);
                }
                break;
            case R.id.upload_answer_two_btn:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
                    //File write logic here
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

                } else {
                    uploadFile(R.id.upload_answer_two_btn);
                }
                break;
            case R.id.upload_answer_three_btn:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
                    //File write logic here
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

                } else {
                    uploadFile(R.id.upload_answer_three_btn);
                }
                break;
            case R.id.upload_answer_four_btn:
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
                    //File write logic here
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

                } else {
                    uploadFile(R.id.upload_answer_four_btn);
                }
                break;
            case R.id.upload_quiz:

                addQuizToDatabse();

                break;
        }
    }


    void uploadFile(int checker) {


        String[] mimeTypes = {"*/*"};
        Intent intent = new Intent()
                .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                .setType("file/*")
                //.setAction(Intent.ACTION_OPEN_DOCUMENT);
                .setAction(Intent.ACTION_GET_CONTENT);


        switch (checker) {
            case R.id.upload_question_btn:
                startActivityForResult(Intent.createChooser(intent, "Select a file"), ACTIVITY_CHOOSE_FILE);

                break;
            case R.id.upload_answer_one_btn:
                startActivityForResult(Intent.createChooser(intent, "Select a file"), ACTIVITY_CHOOSE_ANSWERONE);

                break;
            case R.id.upload_answer_two_btn:
                startActivityForResult(Intent.createChooser(intent, "Select a file"), ACTIVITY_CHOOSE_ANSWERTWO);

                break;
            case R.id.upload_answer_three_btn:
                startActivityForResult(Intent.createChooser(intent, "Select a file"), ACTIVITY_CHOOSE_ANSWERTHREE);

                break;
            case R.id.upload_answer_four_btn:
                startActivityForResult(Intent.createChooser(intent, "Select a file"), ACTIVITY_CHOOSE_ANSWERFOUR);

                break;
        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

                        ref = storageRef.child("Quizzes").child(filename);
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

//                                    successfulTxt.setVisibility(View.VISIBLE);
//                                    initializePlayer(uri);


                                try {
                                    if (requestCode == ACTIVITY_CHOOSE_FILE) {
                                        quizModel.setQuestion(downloadUrl.toString());
                                        uploadQuestionBtn.setClickable(false);
                                        uploadQuestionBtn.setEnabled(false);
                                    } else if (requestCode == ACTIVITY_CHOOSE_ANSWERONE) {
                                        quizModel.setAnswer1(downloadUrl.toString());
                                        uploadAnswerOneBtn.setClickable(false);
                                        uploadAnswerOneBtn.setEnabled(false);
                                    } else if (requestCode == ACTIVITY_CHOOSE_ANSWERTWO) {
                                        quizModel.setAnswer2(downloadUrl.toString());
                                        uploadAnswerTwoBtn.setClickable(false);
                                        uploadAnswerTwoBtn.setEnabled(false);
                                    } else if (requestCode == ACTIVITY_CHOOSE_ANSWERTHREE) {
                                        quizModel.setAnswer3(downloadUrl.toString());
                                        uploadAnswerThreeBtn.setClickable(false);
                                        uploadAnswerThreeBtn.setEnabled(false);
                                    } else if (requestCode == ACTIVITY_CHOOSE_ANSWERFOUR) {
                                        quizModel.setAnswer4(downloadUrl.toString());
                                        uploadAnswerFourBtn.setClickable(false);
                                        uploadAnswerFourBtn.setEnabled(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                    addLessonToDatabse(downloadUrl.toString());


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


    private void addQuizToDatabse() {

        quizModel.setCorrectAnswer(correctanswer.getText().toString());
        if (quizModel.getQuestion() == null || quizModel.getQuestion().equals("")) {

            Toast.makeText(this, "Please Upload The Question Correctly", Toast.LENGTH_SHORT).show();
            return;
        } else if (quizModel.getAnswer1() == null || quizModel.getAnswer1().equals("")) {

            Toast.makeText(this, "Please Upload The First Answer Correctly", Toast.LENGTH_SHORT).show();
            return;
        } else if (quizModel.getAnswer2() == null || quizModel.getAnswer2().equals("")) {

            Toast.makeText(this, "Please Upload The Second Answer Correctly", Toast.LENGTH_SHORT).show();
            return;
        } else if (quizModel.getAnswer3() == null || quizModel.getAnswer3().equals("")) {

            Toast.makeText(this, "Please Upload The Third Answer Correctly", Toast.LENGTH_SHORT).show();
            return;
        } else if (quizModel.getAnswer4() == null || quizModel.getAnswer4().equals("")) {

            Toast.makeText(this, "Please Upload The Fourth Answer Correctly", Toast.LENGTH_SHORT).show();
            return;
        } else if (quizModel.getCorrectAnswer() == null || quizModel.getCorrectAnswer().equals("")) {
            Toast.makeText(this, "Please enter the Correct Answer Number", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if (!(Integer.parseInt(quizModel.getCorrectAnswer()) >= 1 && Integer.parseInt(quizModel.getCorrectAnswer()) <= 4)) {
                Toast.makeText(this, "Please enter the Correct Answer Number", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(this.path);
        mDatabase.child(databasePath).push().setValue(quizModel);
//        mDatabase.push().setValue(quizModel);
        finish();

//        try {
//            Uri uri = Uri.parse(path);
////            initializePlayer(uri);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please press again to upload "
                    , Toast.LENGTH_SHORT).show();
            //resume tasks needing this permission
        } else {
            Toast.makeText(this, "Please you need to allow permission to choose your video to upload it "
                    , Toast.LENGTH_SHORT).show();
        }
    }


}
