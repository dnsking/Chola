package com.app.chalo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.chalo.networking.actions.AddContentNetworkAction;
import com.app.chalo.networking.authentication.GivderContentHelper;
import com.app.chalo.utils.Utils;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lassi.common.utils.KeyUtils;
import com.lassi.data.media.MiMedia;
import com.lassi.domain.media.LassiOption;
import com.lassi.domain.media.MediaType;
import com.lassi.presentation.builder.Lassi;
import com.lassi.presentation.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class NewPubActivity extends AppCompatActivity {


    private static final int  MEDIA_REQUEST_CODE = 1;
    private TextView mediaNameTv,durationTv;
    private TextInputLayout pubTextInputLayout;
    private TextInputEditText pubTextInputEditText;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pub);
        pubTextInputLayout = findViewById(R.id.pubTextInputLayout);
        pubTextInputEditText = findViewById(R.id.pubTextInputEditText);
        mediaNameTv = findViewById(R.id.mediaNameTv);
        durationTv = findViewById(R.id.durationTv);
        findViewById(R.id.selectMediaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMedia();
            }
        });
        findViewById(R.id.uploadBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpload();
            }
        });

    }
    private void selectMedia(){
       Intent intent= new Lassi(this)
                .setMaxCount(1)
                .setGridSize(3)
                .setMediaType(MediaType.AUDIO)
                .setStatusBarColor(R.color.colorPrimaryDark)
                .setToolbarResourceColor(R.color.colorPrimary)
                .setProgressBarColor(R.color.colorAccent)
                .setPlaceHolder(R.drawable.ic_image_placeholder)
                .setErrorDrawable(R.drawable.ic_image_placeholder)
                .setCropType(CropImageView.CropShape.RECTANGLE)
                .build();
        startActivityForResult(intent, MEDIA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null)
             if(MEDIA_REQUEST_CODE==requestCode) {
                  {

                   ArrayList<MiMedia> media=   (ArrayList<MiMedia>)data.getSerializableExtra(KeyUtils.SELECTED_MEDIA);
                      mediaNameTv.setText(media.get(0).getPath());
                      filePath = media.get(0).getPath();
                 //   val selectedMedia = data.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as ArrayList<MiMedia>
                    // Do needful with your selectedMedia
                }
            }
    }
    public void uploadFile(
            String title,String path) throws Exception {

        //File file = new File(Utils.FetchTempDir(NewPubActivity.this), Utils.FetchFileName(NewPubActivity.this, Uri.parse(path)));



        String pubId =Long.toString((long) Math.floor(Math.random() * 90_000_000L) + 10_000_000L) ;
        String url = GivderContentHelper.FetchUploadUrl(pubId+".mp3");
        HttpClient httpclient = new DefaultHttpClient();

        HttpPut httppost = new HttpPut(url);

        httppost.setEntity( new FileEntity(new File(path),"audio/mpeg"));
        HttpResponse response = httpclient.execute(httppost);


        Calendar c = Calendar.getInstance();

        AddContentNetworkAction addContentNetworkAction = new AddContentNetworkAction(
                Utils.GetString(App.ChannelName,NewPubActivity.this), title
                , pubId,Long.toString(c.getTimeInMillis()),Utils.GetUserName(NewPubActivity.this));

        GivderContentHelper.AddContent(NewPubActivity.this,addContentNetworkAction);
        //    FFmpeg.execute(cmd);


    }

    public void startUpload(){

        findViewById(R.id.uploadBtn).setEnabled(false);
        findViewById(R.id.selectMediaBtn).setEnabled(false);
        pubTextInputLayout.setEnabled(false);

        findViewById(R.id.loadingView).setVisibility(View.VISIBLE);
        final String title = pubTextInputEditText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    uploadFile( title,filePath);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           finish();

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            findViewById(R.id.uploadBtn).setEnabled(true);
                            findViewById(R.id.selectMediaBtn).setEnabled(true);
                            pubTextInputLayout.setEnabled(true);

                            findViewById(R.id.loadingView).setVisibility(View.GONE);
                            Snackbar.make( findViewById(R.id.uploadBtn),
                                    "Upload Failed",Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }
}