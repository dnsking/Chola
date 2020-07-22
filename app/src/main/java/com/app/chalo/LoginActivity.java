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

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userIdTextInputEditTextHolder ,userPasswordTextInputLayout;
    private TextInputEditText userIdTextInputEditText, userPasswordTextInputEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userIdTextInputEditTextHolder = findViewById(R.id.userIdTextInputEditTextHolder);
        userPasswordTextInputLayout = findViewById(R.id.userPasswordTextInputLayout);
        userIdTextInputEditText = findViewById(R.id.userIdTextInputEditText);
        userPasswordTextInputEditText = findViewById(R.id.userPasswordTextInputEditText);

        if(Utils.GetUserName(this)!=null){


            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        findViewById(R.id.createBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }
    private void signIn(){

        if(userIdTextInputEditText.getText().toString().length()==0){
            Snackbar.make( findViewById(R.id.createBtn),
                    "User Name Missing",Snackbar.LENGTH_SHORT).show();
        }
        else if(userPasswordTextInputEditText.getText().toString().length()==0){

            Snackbar.make( findViewById(R.id.createBtn),
                    "Password Missing",Snackbar.LENGTH_SHORT).show();
        }
        else{

            userIdTextInputEditTextHolder.setEnabled(false);
            userPasswordTextInputLayout.setEnabled(false);
            findViewById(R.id.loginBtn).setEnabled(false);
            findViewById(R.id.createBtn).setEnabled(false);

            findViewById(R.id.loadingView).setVisibility(View.VISIBLE);
            final CreateChannelNetworkAction createChannelNetworkAction = new CreateChannelNetworkAction(
                    userIdTextInputEditText.getText().toString(),userPasswordTextInputEditText.getText().toString(),"ChannelLogin"
            );
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        GivderContentHelper.AddContent(LoginActivity.this,createChannelNetworkAction);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Utils.SaveUserName(LoginActivity.this,userIdTextInputEditText.getText().toString());

                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
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
                                userPasswordTextInputLayout.setEnabled(true);
                                findViewById(R.id.loginBtn).setEnabled(true);
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