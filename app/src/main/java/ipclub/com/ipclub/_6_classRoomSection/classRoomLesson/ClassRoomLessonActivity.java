package ipclub.com.ipclub._6_classRoomSection.classRoomLesson;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;

public class ClassRoomLessonActivity extends AppCompatActivity implements I_CommonMethodsForWorkingWithServer{
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
        IPC_Application.i().w().classRoomLessons("Android_2016_1", a, token).enqueue(new Callback<Responses<ClassRoomLesson>>() {
            @Override
            public void onResponse(Call<Responses<ClassRoomLesson>> call, retrofit2.Response<Responses<ClassRoomLesson>> response) {

                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        PrettifyHighlighter highlighter = new PrettifyHighlighter();
                        String highlighted = highlighter.highlight("java", response.body().content.content);
                        content.setText(Html.fromHtml(highlighted));
                        lessonTitle.setText(response.body().content.lessonTitle);
                        title.setText(response.body().content.title);
                    }else{
                        showError("Error: "+response.body().message);
                    }

                }else{
                    showError("Something went wrong. "+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<ClassRoomLesson>> call, Throwable t) {
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
}
