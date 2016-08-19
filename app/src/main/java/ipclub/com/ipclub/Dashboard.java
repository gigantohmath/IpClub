package ipclub.com.ipclub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initView();

        auth = new Auth(this);
        auth.checkTokenDate();
    }

    private void initView(){

    }

    public void logout(View v){
        auth.logout();
        finish();
    }

    public void goTo(View v){

        switch (v.getId()){
            case R.id.vocabulary:
                Intent show = new Intent(this, Vocabulary.class);
                startActivity(show);
                break;
        }
    }

}
