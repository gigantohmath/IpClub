package ipclub.com.ipclub._6_classRoomSection.classRoomLesson;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;

public class EditClassRoomLessonActivity extends AppCompatActivity implements I_CommonMethodsForWorkingWithServer {
    private AlertDialog customProgress;
    private Auth auth;
    private EditText content,title;
    private TextView lessonTitle;
    private Button accept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_room_item);
        content = (EditText)findViewById(R.id.classRoomItemEditableBody);
        lessonTitle = (TextView)findViewById(R.id.lessonTitle);
        title = (EditText) findViewById(R.id.editableTitle);
        accept = (Button) findViewById(R.id.acceptBut);
        auth = new Auth(this);
        Intent mIntent = getIntent();
        final int intValue = mIntent.getIntExtra("id",0);
        initCustomLoading();
        getDataFromServerX(intValue);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClassRoomItem(intValue);
            }
        });

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
                        String highlighted ="";
                        if(response.body().content.content == (null) || response.body().content.content.equals("")){

                             highlighted = highlighter.highlight("java", "Content is null");
                        }

                        else{

                             highlighted = highlighter.highlight("java", response.body().content.content);
                        }

                        content.setText(Html.fromHtml(highlighted));
                        lessonTitle.setText(response.body().content.lessonTitle);
                        title.setText(response.body().content.title);
                    }else{
                        showError(EditClassRoomLessonActivity.this.getString(R.string.error)+":"+response.body().message);
                    }

                }else{
                    showError(EditClassRoomLessonActivity.this.getString(R.string.something_went_wrong)+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<ClassRoomLessonContent>> call, Throwable t) {
                loading(false);
                Log.e("MY", "error: " + t.getMessage());
            }
        });


    }

    public void editClassRoomItem(int a){
        loading(true);
        final String token = auth.getToken();
        IPC_Application.i().w().editLessons("Android_2016_1", a, title.getText().toString(),"This is Edited Body from our app.",null,token).enqueue(new Callback<Responses<ArrayList<EditLessonContent>>>() {
            @Override
            public void onResponse(Call<Responses<ArrayList<EditLessonContent>>> call, retrofit2.Response<Responses<ArrayList<EditLessonContent>>> response) {

                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        Intent tempIntent = new Intent(EditClassRoomLessonActivity.this,ClassRoomLessonActivity.class);
                        startActivity(tempIntent);
                        Toast.makeText(EditClassRoomLessonActivity.this, EditClassRoomLessonActivity.this.getString(R.string.edit_success), Toast.LENGTH_SHORT).show();

                    }else{
                        showError(EditClassRoomLessonActivity.this.getString(R.string.error) + ":"+response.body().message);
                    }

                }else{
                    showError(EditClassRoomLessonActivity.this.getString(R.string.something_went_wrong)+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<ArrayList<EditLessonContent>>> call, Throwable t) {
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
                .setTitleText(EditClassRoomLessonActivity.this.getString(R.string.error_dialog_title))
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
