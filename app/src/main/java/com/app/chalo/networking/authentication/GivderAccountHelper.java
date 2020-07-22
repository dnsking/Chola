package com.app.chalo.networking.authentication;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;

import com.app.chalo.App;
import com.app.chalo.networking.actions.NetworkAction;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GivderAccountHelper {
    /***
     * Calls the lambda function's api gateay trigger and returns a response
     * @return
     */
    public static  Response ApiGatewayCaller(NetworkAction flexcineNetworkAction) throws IOException {
        String json = new Gson().toJson(flexcineNetworkAction);

        //  App.Log("test RequestS3Url "+json);
        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                // .addHeader("Authorization",token)
                .url(App.SignInUrl).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Response response = client.newCall(request).execute();
        return response;

    }

    /***
     * Refreshes user's auth token
     * @param authentication
     * @return
     * @throws IOException
     */
    /*
    public static Authentication RefreshToken(Authentication authentication) throws IOException {
            Response response = ApiGatewayCaller(new RefreshTokenNetworkAction(authentication.getRefreshToken()));
            Authentication authenticationResult = new Gson().fromJson(response.body().string(),Authentication.class);

            return authenticationResult;
        }

    public static Authentication LogIn(String userName, String Password) throws IOException {
        Response response = ApiGatewayCaller(
                new LogInNetworkAction(userName
                        ,Password)
        );
        Authentication authenticationResult = new Gson().fromJson(response.body().string(),Authentication.class);

        return authenticationResult;
    }*/

        /***
         * Signs in or signs up the user
         * @param number
         */
        /*
        public static void SignInSignUpToken(String number) throws IOException {

            App.Log("SignInSignUpToken number "+number);
            Response response = ApiGatewayCaller(new SignInNetworkAction(number));
            App.Log("SignInSignUpToken "+response.body().string());
        }
        public static Boolean HasAccount(Context context){
            AccountManager accountManager = AccountManager.get(context);
            return
                    accountManager.getAccountsByType(App.AccountType).length>0;
        }
        public static Authentication FetchAccount(Context context) throws AuthenticatorException, OperationCanceledException, IOException {
            AccountManager accountManager = AccountManager.get(context);
            String authToken =accountManager.blockingGetAuthToken(
                    accountManager.getAccountsByType(App.AccountType)[0], App.TokenType,true);

            App.Log("authToken "+authToken);
            Authentication authentication = new Gson().fromJson(authToken,Authentication.class);

        Authentication newAuthentication  =   LogIn( Utils.GetUserName(context) ,Utils.GetPassword(context));
        if(newAuthentication.getRefreshToken()==null)
            newAuthentication.setRefreshToken(authentication.getRefreshToken());

        String newauthToken = new Gson().toJson(newAuthentication);

        App.Log("newauthToken "+newauthToken);
        if(newAuthentication.getAccessToken()!=null){

            accountManager.setAuthToken(accountManager.getAccountsByType(App.AccountType)[0],App.TokenType,newauthToken);

            return newAuthentication;
        }
        else{
            return authentication;
        }
    }*/
    /***
     * Matches the users verification code and authenticates if it matches. Null if else
     * @param phoneNumber
     * @param userName
     * @param code
     * @return
     * @throws IOException
     */
    /*
    public static Authentication MatchKey(Context context, String phoneNumber, String userName, String code)throws IOException {
        MatchCodeNetworkAction matchCodeNetworkAction = new MatchCodeNetworkAction( phoneNumber, userName, code);
        Response response = ApiGatewayCaller(matchCodeNetworkAction);
        String result = response.body().string();
        App. Log("MatchKey "+result);
        try{

            Authentication authenticationResult = new Gson().fromJson(result,Authentication.class);

            GetPasswordNetworkAction getPasswordNetworkAction = new GetPasswordNetworkAction(matchCodeNetworkAction);
             response = ApiGatewayCaller(getPasswordNetworkAction);
            String password = response.body().string().replaceAll("\"","");

            App. Log("MatchKey password "+password);

            Utils.SavePassword(context,password);

            return authenticationResult;
        }
        catch (Exception ex){
            App. Log("MatchKey password ex"+ex.getMessage());
            return new Authentication();
        }
    }*/
}
