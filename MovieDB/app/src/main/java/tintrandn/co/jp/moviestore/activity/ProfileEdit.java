package tintrandn.co.jp.moviestore.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tintrandn.co.jp.moviestore.R;
import tintrandn.co.jp.moviestore.model.UserProfile;
import tintrandn.co.jp.moviestore.model.storage.dao.UserProfileDao;

public class ProfileEdit extends Activity{
    private static final String TAG = "ProfileEdit";
    private ImageView profile_picture;
    private EditText edt_user_name;
    private EditText edt_user_mail;
    private EditText edt_user_birthday;
    private AppCompatRadioButton radio_male;
    private AppCompatRadioButton radio_female;
    private Calendar myCalendar;
    private String image_path;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_GALLERY = 2;

    @Override
    protected void onResume() {
        UserProfileDao userProfileDao = new UserProfileDao(getApplicationContext());
        if (userProfileDao.checkDataBase()){
            UserProfile user = userProfileDao.getUser();
            if (user !=null) {
                if (user.getUser_image()!=null){
                    File imgFile = new File(user.getUser_image());
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    profile_picture.setImageBitmap(myBitmap);
                }
                edt_user_name.setText(user.getUser_name());
                edt_user_mail.setText(user.getUser_mail());
                edt_user_birthday.setText(user.getUser_birthday());
                String gender = user.getUser_gender();
                if (gender.equals("male")) {
                    radio_male.setChecked(true);
                } else {
                    radio_female.setChecked(true);
                }
            }
        }
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profile_picture = (ImageView) findViewById(R.id.profile_picture);
        edt_user_name = (EditText)findViewById(R.id.edt_user_name);
        edt_user_mail = (EditText)findViewById(R.id.edt_user_mail);
        edt_user_birthday = (EditText)findViewById(R.id.edt_user_birthday);
        Button btn_profile_cancel = (Button) findViewById(R.id.btn_profile_cancel);
        Button btn_profile_done = (Button) findViewById(R.id.btn_profile_done);
        radio_male = (AppCompatRadioButton) findViewById(R.id.radio_male);
        radio_female = (AppCompatRadioButton)findViewById(R.id.radio_female);
        btn_profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_profile_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Done click");
                UserProfileDao userProfileDao = new UserProfileDao(getApplicationContext());
                ContentValues values = new ContentValues();
                if (image_path !=null){
                    values.put(UserProfileDao.COLUMN_IMAGE_PATH,image_path );}
                values.put(UserProfileDao.COLUMN_USER_NAME, String.valueOf(edt_user_name.getText()));
                values.put(UserProfileDao.COLUMN_USER_MAIL, String.valueOf(edt_user_mail.getText()));
                values.put(UserProfileDao.COLUMN_USER_BIRTHDAY, String.valueOf(edt_user_birthday.getText()));
                values.put(UserProfileDao.COLUMN_USER_GENDER, radio_male.isChecked()?"male":"female");
                Log.d(TAG,radio_male.isChecked()?"male":"female");
                if (userProfileDao.update(values,"1") == 0) {
                    Log.d(TAG,"User profile insert");
                    userProfileDao.insert(values);
                }
                finish();
            }
        });

        //set datepick birthday
        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBrithday();
            }

            private void updateBrithday() {
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edt_user_birthday.setText(sdf.format(myCalendar.getTime()));
            }
        };
        edt_user_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileEdit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //=======================================================
        //profile picture listener
        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    //select image
    private void selectImage(){
        final String[] items = {"Gallery","Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEdit.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    //cameraIntent();
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    startActivityForResult(camera_intent, REQUEST_IMAGE_CAPTURE);
                } else if (items[item].equals("Gallery")) {
                    //galleryIntent();
                    Intent gallery_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallery_intent,SELECT_GALLERY);
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_GALLERY)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_IMAGE_CAPTURE)
                onCaptureImageResult(data);
        }
    }
    //set to display image
    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();
        String[]projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplication().getContentResolver().query(uri,projection,null,null,null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        image_path = cursor.getString(columnIndex);
        cursor.close();
        Bitmap profile = BitmapFactory.decodeFile(image_path);
        profile_picture.setImageBitmap(profile);
    }
    //set to display image
    private void onCaptureImageResult(Intent data) {
//        Uri uri = data.getData();
//        String[]projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
//        if (cursor == null) { // Source is Dropbox or other similar local file
//            // path
//            image_path = uri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(projection[0]);
//            image_path = cursor.getString(columnIndex);
//            cursor.close();
//        }
        Bitmap profile = BitmapFactory.decodeFile(image_path);
        Log.d(TAG,"Set Image Path: "+ image_path);
        profile_picture.setImageBitmap(profile);
    }

    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        image_path = file.getAbsolutePath();
        Log.d(TAG,"Image Path: "+ image_path);
        return imgUri;
    }
}
