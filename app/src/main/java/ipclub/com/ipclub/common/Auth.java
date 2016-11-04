package ipclub.com.ipclub.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.dashBoardSection.Dashboard;
import ipclub.com.ipclub.loginSection.LoginContent;
import ipclub.com.ipclub.loginSection.LoginActivity;
import ipclub.com.ipclub.common.responses.Responses;
import ipclub.com.ipclub.utils.IPCProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Auth {

    public Context context;
    public Activity activity;
    public static boolean IS_LOGGED=false;
    private long currentTime;
    private IPCProgressDialog ipcProgressDialog;

    public Auth(Context context) {
        this.context = context;
        activity = (Activity) context;
        currentTime = System.currentTimeMillis();
        ipcProgressDialog = new IPCProgressDialog(context);
    }




    public void login(final String username, final String password){
        if(CheckInternetConnection.isConnected(context)){
            ipcProgressDialog.showIPCProgressDialog();
            IPC_Application.i().w().login(username, password).enqueue(new Callback<Responses<LoginContent>>() {

                @Override
                public void onResponse(Call<Responses<LoginContent>> call, retrofit2.Response<Responses<LoginContent>> response) {
                    ipcProgressDialog.hideIPCProgressDialog();
                    if(response.code() == 200){
                        Log.e("MY", "onResponse() returned: " + response.body().status);

                        String token = response.body().content.token;
                        setToken(token);
                        IS_LOGGED=true;
                        Intent show = new Intent(context, Dashboard.class);
                        context.startActivity(show);
                        activity.finish();


                    }else{
                        Log.e("MY", "onResponse() returned: " + response.code());
                        showRrror(context.getString(R.string.something_went_wrong)+response.code());
                    }

                }

                @Override
                public void onFailure(Call<Responses<LoginContent>> call, Throwable t) {
                    ipcProgressDialog.hideIPCProgressDialog();

                    if(t.getMessage().startsWith("Unable to resolve host")){
                        showRrror(context.getString(R.string.no_internet_text));
                    }
                    else if(t.getMessage().startsWith("Can not deserialize")){
                        showRrror(context.getString(R.string.wrong_username_password));
                    }
                    else{
                        showRrror(context.getString(R.string.something_went_wrong));
                    }
                    Log.e("MY", "error: " + t.getMessage());
                }
            });
        }

    }

    private void showRrror(String text) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }

    public boolean checkLoggedIn(){
        String token = IPC_Application.i().getPreferences().getToken();

        if(token.equals("")){
            return false;
        }
        return true;
    }

    public void logout(){
        IPC_Application.i().getPreferences().clear();
        Intent show = new Intent(context, LoginActivity.class);
        context.startActivity(show);
    }

    private void setToken(String token){

        IPC_Application.i().getPreferences().setToken(token);
        IPC_Application.i().getPreferences().setTokenDate(currentTime);
    }

    private void updateTokenDate(){
        IPC_Application.i().getPreferences().setTokenDate(currentTime);
    }

    public void checkTokenDate(){
        String token = IPC_Application.i().getPreferences().getToken();
        long lastUse = IPC_Application.i().getPreferences().getTokenDate();

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
        String token = IPC_Application.i().getPreferences().getToken();

        if(token.equals("")){
            return null;
        }
        updateTokenDate();
        return token;
    }

    private void refreshToken(String oldToken){
        if(CheckInternetConnection.isConnected(context)){
            ipcProgressDialog.showIPCProgressDialog();
            IPC_Application.i().w().refreshToken(oldToken).enqueue(new Callback<Responses<LoginContent>>() {
                @Override
                public void onResponse(Call<Responses<LoginContent>> call, Response<Responses<LoginContent>> response) {
                    ipcProgressDialog.hideIPCProgressDialog();
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
                    ipcProgressDialog.hideIPCProgressDialog();
                }
            });
        }

    }
}
