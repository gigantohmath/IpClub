package ipclub.com.ipclub._3_dashBoardSection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ipclub.com.ipclub.R;
import ipclub.com.ipclub._1_loginSection.A;
import ipclub.com.ipclub._2_changePasswordSection.ChangePasswordActivity;
import ipclub.com.ipclub._5_docsSection.DocsActivity;
import ipclub.com.ipclub._5_docsSection.DocsContent;
import ipclub.com.ipclub._5_docsSection.docsItem.DocsItemActivity;
import ipclub.com.ipclub._6_classRoomSection.ClassRoomActivity;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub._4_vocabularySection.Vocabulary;

public class Dashboard extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST=101 ;

    private static final int MY_PERMISSIONS_REQUEST_2=102 ;
    private Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        checkForPermissions();

        auth = new Auth(this);
        auth.checkTokenDate();

        initView();

    }

    public void checkForPermissions(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            Toast.makeText(this, "Not Granted !", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);

        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            Toast.makeText(this, "Not Granted !", Toast.LENGTH_SHORT).show();

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_2);

        }

        else{

            Toast.makeText(this, "Granted !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Granted WRITE!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "NOT Granted WRITE!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Granted READ!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "NOT Granted READ!", Toast.LENGTH_SHORT).show();
                }
                return;
            }

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

    private void initView(){

    }

    public void goTo(View v){
        Intent show;
        switch (v.getId()){
            case R.id.vocabulary:
                 show = new Intent(this, Vocabulary.class);
                startActivity(show);

                break;
            case  R.id.classRoom:
                show = new Intent(this, ClassRoomActivity.class);
                startActivity(show);

                break;
            case  R.id.docs:
                show = new Intent(this, DocsActivity.class);
                startActivity(show);
                break;

            case R.id.settings:
                show=new Intent(this, ChangePasswordActivity.class);
                startActivity(show);
                break;


        }
    }

}
