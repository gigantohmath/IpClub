package ipclub.com.ipclub._1_loginSection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub._3_dashBoardSection.Dashboard;
import ipclub.com.ipclub.*;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();

        auth = new Auth(this);

        if(auth.checkLoggedIn()){
            Intent show = new Intent(this, Dashboard.class);
            startActivity(show);

        }

    }

    @Override
    public void onBackPressed() {
        Intent launchNextActivity;
        launchNextActivity = new Intent(this, A.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(launchNextActivity);
    }
    public void onLoginButtonClick(View v){

        hideKeyboard();
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if(user.equals("") || pass.equals("")){
            showError(getResources().getString(R.string.fill_all_fields));
        }else{
            auth.login(user, pass);
        }

    }

    private void initView(){
        username = (EditText) findViewById(R.id.username_et);
        password = (EditText) findViewById(R.id.password_et);
    }

    private void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getResources().getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }

    private void hideKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager) this.
                        getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
