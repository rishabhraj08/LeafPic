package org.horaapps.leafpic.activities;
/*
File mydir = context.getDir("mydir", Context.MODE_PRIVATE); //Creating an internal dir;
File fileWithinMyDir = new File(mydir, "myfile"); //Getting a file within the dir.
FileOutputStream out = new FileOutputStream(fileWithinMyDir); //Use the stream as usual to write into the file.
 */
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View;
import android.support.v4.content.ContextCompat;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.view.IconicsImageView;
//import com.dropbox.core.android.Auth;

import org.horaapps.leafpic.FaceRecognition.FaceRecog;
import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.MainActivity;
import org.horaapps.leafpic.util.CustomTabService;
import org.horaapps.leafpic.util.StringUtils;
import org.horaapps.liz.ThemedActivity;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Saksham.
 */

public class DropboxBackup extends ThemedActivity {

    //AppKey for Dropbox
    final static private String APP_KEY = "3nmmgn5jsq5i5o4";

    //AppSecret for Dropbox
    final static private String APP_SECRET = "fh2s0423qxzc5e5";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    //For Firebase

    private StorageReference mStorageRef;
    private static final int GALLERY_INTENT = 2;
    ProgressDialog mprogress;
    ProgressDialog dbprogress;


    private Toolbar toolbar;
    private CustomTabService cts;
    private ScrollView scr;
    private Button btnDropbox;
    private Button btnFirebase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_backup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        scr = (ScrollView)findViewById(R.id.backupAct_scrollView);
        btnDropbox = findViewById(R.id.dropbox_sign_in);
        btnFirebase = findViewById(R.id.firebase_sign_in);

        AppKeyPair appkeys = new AppKeyPair(APP_KEY,APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appkeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        btnFirebase = (Button)findViewById(R.id.firebase_sign_in);
        mprogress = new ProgressDialog(this);

        iniUi();
        cts = new CustomTabService(DropboxBackup.this);
    }

    private void iniUi() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getToolbarIcon(GoogleMaterial.Icon.gmd_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        btnDropbox.setText(getString(R.string.dropbox_login).toUpperCase());
        btnFirebase.setText(getString(R.string.firebase_login).toUpperCase());
        btnDropbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                initialize_session();
            }
        });

        btnFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK)
        {
            mprogress.setMessage("Uploading");
            mprogress.show();
            final Uri uri = data.getData();
            StorageReference filepath = mStorageRef.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(DropboxBackup.this,"Upload Done!",Toast.LENGTH_LONG).show();
                    mprogress.dismiss();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    //Uri res  = filepath.getDownloadUrl().getResult();
                   // Intent browser = new Intent(Intent.ACTION_VIEW, downloadUri);
                    //startActivity(browser);
                    Intent it=new Intent(getBaseContext(), FaceRecog.class);
                    it.putExtra("uri",downloadUri.toString());
                    startActivity(it);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    /*@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        AppKeyPair appkeys = new AppKeyPair(APP_KEY,APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appkeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        setContentView(R.layout.dropbox_backup);

        Button SignInButton = (Button)findViewById(R.id.dropbox_sign_in);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize_session();

            }
        });
    }*/

    protected void initialize_session()
    {
        try
        {
            mDBApi.getSession().startOAuth2Authentication(getApplicationContext());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mDBApi.getSession().authenticationSuccessful())
        {
            try
            {
                mDBApi.getSession().finishAuthentication();
                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
            }
            catch (IllegalStateException e)
            {
                Log.i("DbAuthLog","Error authenticating",e);
            }
        }
    }

    public void uploadFiles(View view)
    {
        new Upload().execute();
    }
    public class Upload extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {

        }
        protected String doInBackground(String... arg0)
        {
            DropboxAPI.Entry response = null;
            try {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);

                File file = new File("/storage/emulated/0/DCIM/Camera/IMG_20170920_140029.jpg");
                FileInputStream inputStream = new FileInputStream(file);
                response = mDBApi.putFile("/storage/emulated/0/DCIM/Camera/IMG_20170920_140029.jpg", inputStream, file.length(), null, null);
                Log.e("DbExampleLog", "The uploaded files recv is:" + response.rev);
                dbprogress.setMessage("Uploading");
                dbprogress.show();


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return response.rev;
        }




        @Override
        protected void onPostExecute(String result)
        {
            if(result.isEmpty()==false)
            {
                Toast.makeText(getApplicationContext(),"File Uploaded",Toast.LENGTH_LONG).show();
                Log.e("DbExamplrLog","The uploaded file rev is : "+result);
                dbprogress.dismiss();
            }
        }

    }


    @CallSuper
    @Override
    public void updateUiElements() {
        super.updateUiElements();
        toolbar.setBackgroundColor(getPrimaryColor());
        setStatusBarColor();
        setNavBarColor();
        themeButton(btnDropbox);
        themeButton(btnFirebase);
        setRecentApp(getString(R.string.cloud));

        ((TextView) findViewById(R.id.backup_title)).setTextColor(getAccentColor());
        ((TextView) findViewById(R.id.dropbox_item_title)).setTextColor(getAccentColor());
        ((TextView) findViewById(R.id.backup_firebase_item_title)).setTextColor(getAccentColor());

        findViewById(R.id.backup_background).setBackgroundColor(getBackgroundColor());

        int color = getCardBackgroundColor();
        ((CardView) findViewById(R.id.backup_header_card)).setCardBackgroundColor(color);
        ((CardView) findViewById(R.id.dropbox_card)).setCardBackgroundColor(color);
        ((CardView) findViewById(R.id.cloud_firebase_card)).setCardBackgroundColor(color);

        color = getIconColor();
        ((IconicsImageView) findViewById(R.id.dropbox_icon_title)).setColor(color);
        ((IconicsImageView) findViewById(R.id.backup_firebase_icon_title)).setColor(color);
        ((IconicsImageView) findViewById(R.id.backup_header_icon)).setColor(color);

        color = getTextColor();
        ((TextView) findViewById(R.id.backup_dropbox_item)).setTextColor(color);
        ((TextView) findViewById(R.id.backup_firebase_item)).setTextColor(color);
        ((TextView) findViewById(R.id.backup_header_item)).setTextColor(color);

        setScrollViewColor(scr);
    }


}
