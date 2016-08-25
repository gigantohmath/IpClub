package ipclub.com.ipclub._2_changePasswordSection;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub._3_dashBoardSection.Dashboard;
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
    private EditText password_et;
    private EditText newPssword_et;
    private EditText repeatPassword_et;
    private String password;
    private String newPassword;
    private String repeatPassword;
    private Auth auth;
    private  AlertDialog customProgress;
    private SweetAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        auth = new Auth(this);
        initCustomLoading();
        initView();
    }

    private void initView() {
        password_et=(EditText)findViewById(R.id.password_change_password);
        newPssword_et=(EditText)findViewById(R.id.new_password_change_password);
        repeatPassword_et=(EditText)findViewById(R.id.repeat_password_change_password);
    }

    public void onSaveChangesButtonClick(View v){
        password=password_et.getText().toString();
        newPassword=newPssword_et.getText().toString();
        repeatPassword=repeatPassword_et.getText().toString();
        if(password.equals("") || newPassword.equals("") || repeatPassword.equals("")){
            showError("Please, fill all fields!");
        }
        else if(!newPassword.equals(repeatPassword)){
            showError("new passwords don't match");
        }
        else{
            sendDataToServer(new String[]{password,newPassword});
        }

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
                            showSuccess();
                        }
                        Log.e("MY", response.body().message);
                    }
                    else if(response.body().status == 401){
                        showError("Wrong password");
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

    public void onLogoutImageClick(View v){
        showLogoutDialog();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,Dashboard.class);
        finish();
        startActivity(intent);
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

    public void showLogoutDialog(){
        dialog= new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Logging out?");
        dialog.setContentText("Are you sure you want to logout ?");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Auth.IS_LOGGED=false;
                auth.logout();
                finish();


            }
        });
        dialog.showCancelButton(true);
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.cancel();
            }
        });
        dialog.setCancelText("cancel");
        dialog.show();
    }


    public void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }

    public void showSuccess(){
        new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent goToDashboard=new Intent(ChangePasswordActivity.this, Dashboard.class);
                        startActivity(goToDashboard);
                    }
                })
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
