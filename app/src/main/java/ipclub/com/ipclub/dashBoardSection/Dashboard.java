package ipclub.com.ipclub.dashBoardSection;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.CheckInternetConnection;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.responses.Responses;
import ipclub.com.ipclub.docsSection.docsItem.DocsItemActivity;
import ipclub.com.ipclub.docsSection.docsItem.DocsItemFileContent;
import ipclub.com.ipclub.enableCourses.EnableCoursesContent;
import ipclub.com.ipclub.loginSection.A;
import ipclub.com.ipclub.changePasswordSection.ChangePasswordActivity;
import ipclub.com.ipclub.docsSection.DocsActivity;
import ipclub.com.ipclub.classRoomSection.ClassRoomActivity;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.utils.IPCProgressDialog;
import ipclub.com.ipclub.vocabularySection.Vocabulary;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST=101 ;

    private static final int MY_PERMISSIONS_REQUEST_2=102 ;
    private Auth auth;
    private SweetAlertDialog dialog;
    private IPCProgressDialog ipcProgressDialog;
    private List<String> courses;
    private TextView course_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        checkForPermissions();
        ipcProgressDialog = new IPCProgressDialog(this);
        auth = new Auth(this);
        auth.checkTokenDate();
        getEnabledCourses();
        course_tv = (TextView) findViewById(R.id.course_name);
        course_tv.setText(IPC_Application.i().getPreferences().getSelectedCourse());

    }

    private void getEnabledCourses() {
        if(CheckInternetConnection.isConnected(this)){
            String token = auth.getToken();
            ipcProgressDialog.showIPCProgressDialog();
            IPC_Application.i().w().enabledCourses(token).enqueue(new Callback<Responses<EnableCoursesContent>>() {
                @Override
                public void onResponse(Call<Responses<EnableCoursesContent>> call, Response<Responses<EnableCoursesContent>> response) {
                    ipcProgressDialog.hideIPCProgressDialog();
                    if(response.code() == 200){
                        if(response.body().status == 200){
                            EnableCoursesContent content = response.body().content;
                            courses = content.course;
                            Log.e("",""+content.course);
                        }else{
                            showError("Error: "+response.body().message);
                        }
                    }else{
                        showError("Something went wrong. "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<Responses<EnableCoursesContent>> call, Throwable t) {
                    ipcProgressDialog.hideIPCProgressDialog();
                }

            });
        }else{
            showError(getResources().getString(R.string.no_internet_text));
        }

    }

    public void checkForPermissions(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {



                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);

        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {



            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_2);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
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

    public void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }

    public void onLogoutImageClick(View view) {
        showLogoutDialog();
    }
    public void showLogoutDialog(){
        dialog= new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(this.getString(R.string.log_out_dialog_title));
        dialog.setContentText(this.getString(R.string.log_out_dialog_text));
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
        dialog.setCancelText(this.getString(R.string.dialog_cancel));
        dialog.show();
    }

    public void chooseCourse(View view) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.choose_course));
        String[] values = courses.toArray(new String[0]);
        b.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                course_tv.setText(courses.get(which));
                IPC_Application.i().getPreferences().setSelectedCourse(courses.get(which));
            }
        });
        b.create().show();
    }
}
