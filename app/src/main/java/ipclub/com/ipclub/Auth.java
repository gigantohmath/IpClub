package ipclub.com.ipclub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.contents.LoginContent;
import ipclub.com.ipclub.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Artak on 8/14/2016.
 */
public class Auth {

    public Context context;
    public Activity activity;
    SweetAlertDialog pLoading;

    public  static final String TOKEN_PREFERENCE = "access_token";
    public  static final String TOKEN = "token";
    public  static final String DATE = "date";
    private long currentTime;
    public  SharedPreferences sharedpref;

    public Auth(Context context) {
        this.context = context;
        activity = (Activity) context;
        initLoading();
        sharedpref = context.getSharedPreferences(TOKEN_PREFERENCE, context.MODE_PRIVATE);
        currentTime = System.currentTimeMillis();
    }


    public void login(final String username, final String password){
        loading(true);
        IPC_Application.i().w().login(username, password).enqueue(new Callback<Responses<LoginContent>>() {

            @Override
            public void onResponse(Call<Responses<LoginContent>> call, retrofit2.Response<Responses<LoginContent>> response) {
                loading(false);
                if(response.code() == 200){
                    Log.e("MY", "onResponse() returned: " + response.body().status);

                    String token = response.body().content.token;
                    setToken(token);

                    Intent show = new Intent(context, Dashboard.class);
                    context.startActivity(show);
                    activity.finish();


                }else{
                    Log.e("MY", "onResponse() returned: " + response.code());
                    showRrror("Something went wrong. "+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<LoginContent>> call, Throwable t) {
                loading(false);

                if(t.getMessage().startsWith("Unable to resolve host")){
                    showRrror("Where is your internet?");
                }else{
                    showRrror("Something went wrong.");
                }
                Log.e("MY", "error: " + t.getMessage());
            }
        });
    }

    private void showRrror(String text) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }

    public boolean checkLoggedIn(){
        String token = sharedpref.getString(TOKEN, "");

        if(token.equals("")){
            return false;
        }
        return true;
    }

    public void logout(){
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.clear();
        editor.commit();

        Intent show = new Intent(context, MainActivity.class);
        context.startActivity(show);
    }

    private void setToken(String token){

        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString(TOKEN, token);
        editor.putLong(DATE, currentTime);
        editor.commit();
    }

    private void updateTokenDate(){

        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putLong(DATE, currentTime);
        editor.commit();

    }

    public void checkTokenDate(){
        String token = sharedpref.getString(TOKEN, "");
        long lastUse = sharedpref.getLong(DATE, 0L);

        if(lastUse == 0L){
            logout();
        }
        Date currentDate = new Date(currentTime);
        Date lastUpdateDate = new Date(lastUse);

        long difference = currentDate.getTime() - lastUpdateDate.getTime();
        long differenceInMinutes = TimeUnit.MILLISECONDS.toMinutes(difference);

        Log.e("MY", "DATE: "+differenceInMinutes);
        if (differenceInMinutes > 1400){
            Log.e("MY", "TOKEN EXPIRED");
            refreshToken(token);
        }
    }

    @Nullable
    public String getToken(){
        String token = sharedpref.getString(TOKEN, "");

        if(token.equals("")){
            return null;
        }
        updateTokenDate();
        return token;
    }

    private void refreshToken(String oldToken){
        loading(true);
        IPC_Application.i().w().refreshToken(oldToken).enqueue(new Callback<Responses<LoginContent>>() {
            @Override
            public void onResponse(Call<Responses<LoginContent>> call, Response<Responses<LoginContent>> response) {
                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        String token = response.body().content.token;
                        Log.e("MY", "NEW TOKEN: "+token);
                        setToken(token);
                    }
                }
            }

            @Override
            public void onFailure(Call<Responses<LoginContent>> call, Throwable t) {
                loading(false);
            }
        });
    }

    private void initLoading(){
        pLoading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pLoading.setTitleText("Loading");
        pLoading.setCancelable(false);
    }

    private void loading(boolean show){
        if (show){
            pLoading.show();
        }else {
            pLoading.hide();
        }
    }
}
