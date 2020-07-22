package com.app.chalo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.chalo.networking.actions.CreateChannelNetworkAction;
import com.app.chalo.networking.authentication.GivderContentHelper;
import com.app.chalo.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout userIdTextInputEditTextHolder,channelNameTextInputEditTextHolder,aboutTextInputEditTextHolder
            ,userPasswordTextInputLayout;
    private TextInputEditText userIdTextInputEditText,channelNameTextInputEditText,aboutTextInputEditText,
            userPasswordTextInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userIdTextInputEditTextHolder = findViewById(R.id.userIdTextInputEditTextHolder);
        channelNameTextInputEditTextHolder = findViewById(R.id.channelNameTextInputEditTextHolder);
        aboutTextInputEditTextHolder = findViewById(R.id.aboutTextInputEditTextHolder);
        userPasswordTextInputLayout = findViewById(R.id.userPasswordTextInputLayout);

        userIdTextInputEditText = findViewById(R.id.userIdTextInputEditText);
        channelNameTextInputEditText = findViewById(R.id.channelNameTextInputEditText);
        aboutTextInputEditText = findViewById(R.id.aboutTextInputEditText);
        userPasswordTextInputEditText = findViewById(R.id.userPasswordTextInputEditText);


        findViewById(R.id.createBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }
    private long generateRandomNumber(int n) {
        long min = (long) Math.pow(10, n - 1);
        return ThreadLocalRandom.current().nextLong(min, min * 10);
    }
    private void signUp(){

        if(userIdTextInputEditText.getText().toString().length()==0){
            Snackbar.make( findViewById(R.id.createBtn),
                    "User Name Missing",Snackbar.LENGTH_SHORT).show();
        }
        else if(userPasswordTextInputEditText.getText().toString().length()==0){

            Snackbar.make( findViewById(R.id.createBtn),
                    "Password Missing",Snackbar.LENGTH_SHORT).show();
        }
        else if(aboutTextInputEditText.getText().toString().length()==0){
            Snackbar.make( findViewById(R.id.createBtn),
                    "About Missing",Snackbar.LENGTH_SHORT).show();}
        else{

            String channelId = Long.toString(generateRandomNumber(4));
            userIdTextInputEditTextHolder.setEnabled(false);
            channelNameTextInputEditTextHolder.setEnabled(false);
            aboutTextInputEditTextHolder.setEnabled(false);
            userPasswordTextInputLayout.setEnabled(false);
            findViewById(R.id.createBtn).setEnabled(false);

            findViewById(R.id.loadingView).setVisibility(View.VISIBLE);


            Utils.SaveString(App.Password,SignUpActivity.this,
                    userPasswordTextInputEditText.getText().toString());
            Utils.SaveString(App.ChannelName,SignUpActivity.this,channelId.toString());
            Utils.SaveString(App.About,SignUpActivity.this,
                    aboutTextInputEditText.getText().toString());



            Calendar c = Calendar.getInstance();
            final CreateChannelNetworkAction createChannelNetworkAction =new CreateChannelNetworkAction(channelId,
                    userIdTextInputEditText.getText().toString(),
                    aboutTextInputEditText.getText().toString(),Long.toString(c.getTimeInMillis()),

                    userPasswordTextInputEditText.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        GivderContentHelper.AddContent(SignUpActivity.this,createChannelNetworkAction);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Utils.SaveUserName(SignUpActivity.this,userIdTextInputEditText.getText().toString());

                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                finish();
                            }
                        });
                    }
                    catch (Exception ex){
                        App.Log("Sign up failed "+ex.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                userIdTextInputEditTextHolder.setEnabled(true);
                                channelNameTextInputEditTextHolder.setEnabled(true);
                                aboutTextInputEditTextHolder.setEnabled(true);
                                userPasswordTextInputLayout.setEnabled(true);
                                findViewById(R.id.createBtn).setEnabled(true);

                                findViewById(R.id.loadingView).setVisibility(View.GONE);
                                Snackbar.make( findViewById(R.id.createBtn),
                                        "Account Creation Failed",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }).start();
        }

    }
}