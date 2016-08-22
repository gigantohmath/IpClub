package ipclub.com.ipclub._2_changePasswordSection;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.EmptyContent;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements I_CommonMethodsForWorkingWithServer {
    private Auth auth;
    private  AlertDialog customProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        auth = new Auth(this);
    }

 @Override
    public void sendDataToServer(String[] reqParams){
        String token = auth.getToken();
        loading(true);
        IPC_Application.i().w().changePassword(reqParams[0], reqParams[1], token).enqueue(new Callback<Responses<ArrayList<EmptyContent>>>() {
            @Override
            public void onResponse(Call<Responses<ArrayList<EmptyContent>>> call, Response<Responses<ArrayList<EmptyContent>>> response) {
                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        if (response.body().message.startsWith("Password successfully")){

                        }
                        Log.e("MY", response.body().message);
                    }
                }
            }

            @Override
            public void onFailure(Call<Responses<ArrayList<EmptyContent>>> call, Throwable t) {
                loading(false);
                showError(t.getMessage()+"");
                Log.e("MY", t.getMessage()+"");
            }
        });
    }


    @Override
    public void initCustomLoading() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_progress, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);


        customProgress = dialogBuilder.create();
        customProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }

    @Override
    public void loading(boolean show) {
        if (show){
            customProgress.show();
        }else {
            customProgress.hide();
        }
    }


    @Override
    public void getDataFromServer() {

    }
}
