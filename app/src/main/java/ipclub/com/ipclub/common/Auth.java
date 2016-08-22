package ipclub.com.ipclub.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub._3_dashBoardSection.Dashboard;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub._1_loginSection.LoginContent;
import ipclub.com.ipclub._1_loginSection.LoginActivity;
import ipclub.com.ipclub.common.responses.Responses;
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
    AlertDialog customProgress;

    public  static final String TOKEN_PREFERENCE = "access_token";
    public  static final String TOKEN = "token";
    public  static final String DATE = "date";
    private long currentTime;
    public  SharedPreferences sharedpref;

    public Auth(Context context) {
        this.context = context;
        activity = (Activity) context;
        //initLoading();
        initCustomLoading();
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

        Intent show = new Intent(context, LoginActivity.class);
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

    private void initCustomLoading() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_progress, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        customProgress = dialogBuilder.create();
        customProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void loading(boolean show){
        if (show){
            //pLoading.show();
            customProgress.show();
        }else {
            //pLoading.hide();
            customProgress.hide();
        }
    }
}
