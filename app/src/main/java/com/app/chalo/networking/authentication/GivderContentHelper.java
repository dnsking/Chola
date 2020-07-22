package com.app.chalo.networking.authentication;

import android.content.Context;

import com.app.chalo.App;
import com.app.chalo.networking.actions.GetContentNetworkAction;
import com.app.chalo.networking.actions.NetworkAction;
import com.app.chalo.networking.actions.PutUrlAction;
import com.app.chalo.networking.actions.UserNetworkAction;
import com.google.gson.Gson;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GivderContentHelper {


    public static String FetchUploadUrl(String key) throws Exception {



        Gson gson = new Gson();
        String json = gson.toJson(new PutUrlAction(key));


        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(App.Urls.Content).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Response response = client.newCall(request).execute();
        String url = response.body().string().replace("\"", "");
        App.Log("FetchUploadUrl url "+url);
        return url;
    }

    public static String AddContent(Context context, NetworkAction userNetworkAction) throws Exception {


       // Authentication authentication = GivderAccountHelper.FetchAccount(context);

        Gson gson = new Gson();
        String json = gson.toJson( userNetworkAction);

          App.Log("test AddContent "+json);
        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
              //  .addHeader("Authorization",authentication.getIdToken())
                .url(App.Urls.Content).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Response response = client.newCall(request).execute();
        String responseString =response.body().string().replace("\"", "");;
        App.Log("AddContent "+responseString);
        return responseString;
    }
    public static UserNetworkAction[] GetContent(Context context) throws Exception {


        //Authentication authentication = GivderAccountHelper.FetchAccount(context);

        Gson gson = new Gson();
        String json = gson.toJson( new GetContentNetworkAction());

        //  App.Log("test RequestS3Url "+json);
        OkHttpClient client =  new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
           //     .addHeader("Authorization",authentication.getIdToken())
                .url(App.Urls.Content).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json))
                .build();
        Response response = client.newCall(request).execute();
        String responseString =response.body().string();
        App.Log("GetContent "+responseString);
        return new Gson().fromJson(responseString, UserNetworkAction[].class);
    }
}
