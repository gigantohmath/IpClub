package ipclub.com.ipclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        auth = new Auth(this);

        if(auth.checkLoggedIn()){
            Intent show = new Intent(this, Dashboard.class);
            startActivity(show);
            finish();
        }

    }



    public void loginSubmit(View v){
        hideKeyboard();
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if(user.equals("") || pass.equals("")){
            showRrror("Please, fill all fields.");
        }else{
            auth.login(user, pass);
        }

    }

    private void initView(){
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
    }

    private void showRrror(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
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
