package ipclub.com.ipclub.changePasswordSection;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.dashBoardSection.Dashboard;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.EmptyContent;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.NavigationItemSelector;
import ipclub.com.ipclub.common.responses.Responses;
import ipclub.com.ipclub.utils.IPCProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity
        implements I_CommonMethodsForWorkingWithServer, NavigationView.OnNavigationItemSelectedListener {
    private EditText password_et;
    private EditText newPssword_et;
    private EditText repeatPassword_et;
    private String password;
    private String newPassword;
    private String repeatPassword;
    private Auth auth;
    private SweetAlertDialog dialog;
    private IPCProgressDialog ipcProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        auth = new Auth(this);
        initView();
        ipcProgressDialog = new IPCProgressDialog(this);
    }

    private void initView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.change_password_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        password_et=(EditText)findViewById(R.id.password_change_password);
        newPssword_et=(EditText)findViewById(R.id.new_password_change_password);
        repeatPassword_et=(EditText)findViewById(R.id.repeat_password_change_password);
    }

    public void onSaveChangesButtonClick(View v){
        password=password_et.getText().toString();
        newPassword=newPssword_et.getText().toString();
        repeatPassword=repeatPassword_et.getText().toString();
        if(password.equals("") || newPassword.equals("") || repeatPassword.equals("")){
            showError(this.getString(R.string.fill_all_fields));
        }
        else if(!newPassword.equals(repeatPassword)){
            showError(this.getString(R.string.new_passwords_dont_match));
        }
        else{
            sendDataToServer(new String[]{password,newPassword});
        }

    }

    @Override
    public void sendDataToServer(String[] reqParams){
        String token = auth.getToken();
        ipcProgressDialog.showIPCProgressDialog();
        IPC_Application.i().w().changePassword(reqParams[0], reqParams[1], token).enqueue(new Callback<Responses<ArrayList<EmptyContent>>>() {
            @Override
            public void onResponse(Call<Responses<ArrayList<EmptyContent>>> call, Response<Responses<ArrayList<EmptyContent>>> response) {
                ipcProgressDialog.hideIPCProgressDialog();
                if(response.code() == 200){
                    if(response.body().status == 200){
                        if (response.body().message.startsWith("Password successfully")){
                            showSuccess();
                        }
                        Log.e("MY", response.body().message);
                    }
                    else if(response.body().status == 401){
                        showError(ChangePasswordActivity.this.getString(R.string.wrong_old_password));
                    }
                }
            }

            @Override
            public void onFailure(Call<Responses<ArrayList<EmptyContent>>> call, Throwable t) {
                ipcProgressDialog.hideIPCProgressDialog();
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


    public void showLogoutDialog(){
        dialog= new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(ChangePasswordActivity.this.getString(R.string.log_out_dialog_title));
        dialog.setContentText(ChangePasswordActivity.this.getString(R.string.log_out_dialog_text));
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Auth.IS_LOGGED=false;
                auth.logout();
                dialog.dismiss();
            }
        });
        dialog.showCancelButton(true);
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        });
        dialog.setCancelText(ChangePasswordActivity.this.getString(R.string.dialog_cancel));
        dialog.show();
    }


    public void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(ChangePasswordActivity.this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }

    public void showSuccess(){
        new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(ChangePasswordActivity.this.getString(R.string.success_dialog_title))
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
    public void getDataFromServer() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        NavigationItemSelector n = new NavigationItemSelector();
        n.doSelectedAction(this, id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.change_password_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
