package ipclub.com.ipclub._6_classRoomSection.classRoomLesson;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.NavigationItemSelector;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;

public class ClassRoomLessonActivity extends AppCompatActivity
        implements I_CommonMethodsForWorkingWithServer, NavigationView.OnNavigationItemSelectedListener{
    private AlertDialog customProgress;
    private Auth auth;
    private TextView content,lessonTitle,title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room_lesson);
        content = (TextView)findViewById(R.id.classRoomItemBody);
        lessonTitle = (TextView)findViewById(R.id.lessonTitle);
        title = (TextView)findViewById(R.id.title);

        NavigationView navigationView = (NavigationView) findViewById(R.id.classroom_lessons_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        auth = new Auth(this);
        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("id",0);
        initCustomLoading();
        getDataFromServerX(intValue);
    }
    public void getDataFromServerX(int a){
        loading(true);
        final String token = auth.getToken();
        Log.e("MYTOKEN",token);
        IPC_Application.i().w().classRoomLessons("Android_2016_1", a, token).enqueue(new Callback<Responses<ClassRoomLessonContent>>() {
            @Override
            public void onResponse(Call<Responses<ClassRoomLessonContent>> call, retrofit2.Response<Responses<ClassRoomLessonContent>> response) {

                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        PrettifyHighlighter highlighter = new PrettifyHighlighter();
                        String highlighted = highlighter.highlight("java", response.body().content.content);
                        content.setText(Html.fromHtml(highlighted));
                        lessonTitle.setText(response.body().content.lessonTitle);
                        title.setText(response.body().content.title);
                    }else{
                        showError(ClassRoomLessonActivity.this.getString(R.string.error)+":"+response.body().message);
                    }

                }else{
                    showError(ClassRoomLessonActivity.this.getString(R.string.something_went_wrong)+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<ClassRoomLessonContent>> call, Throwable t) {
                loading(false);
                Log.e("MY", "error: " + t.getMessage());
            }
        });


    }
    @Override
    public void getDataFromServer() {

    }

    @Override
    public void sendDataToServer(String... strings) {

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
        .setTitleText(ClassRoomLessonActivity.this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }

    @Override
    public void loading(boolean show) {
        if (show){
            if(customProgress == null){
                initCustomLoading();
            }
            customProgress.show();
        }else {
            customProgress.hide();
            customProgress.dismiss();
            customProgress = null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        NavigationItemSelector n = new NavigationItemSelector();
        n.doSelectedAction(this, id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.classroom_lessons_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
