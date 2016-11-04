package ipclub.com.ipclub.classRoomSection.classRoomLesson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.CheckInternetConnection;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.responses.Responses;
import ipclub.com.ipclub.utils.IPCProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class EditClassRoomLessonActivity extends AppCompatActivity implements I_CommonMethodsForWorkingWithServer {
    private Auth auth;
    private EditText content,title;
    private TextView lessonTitle;
    private Button accept;
    private IPCProgressDialog ipcProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_room_item);
        content = (EditText)findViewById(R.id.classRoomItemEditableBody);
        lessonTitle = (TextView)findViewById(R.id.lessonTitle);
        title = (EditText) findViewById(R.id.editableTitle);
        accept = (Button) findViewById(R.id.acceptBut);
        auth = new Auth(this);
        ipcProgressDialog = new IPCProgressDialog(this);
        Intent mIntent = getIntent();
        final int intValue = mIntent.getIntExtra("id",0);
        getDataFromServerX(intValue);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClassRoomItem(intValue);
            }
        });

    }
    public void getDataFromServerX(int a){
        if(CheckInternetConnection.isConnected(this)){
            ipcProgressDialog.showIPCProgressDialog();
            final String token = auth.getToken();
            Log.e("MYTOKEN",token);
            String course =  IPC_Application.i().getPreferences().getSelectedCourse();
            IPC_Application.i().w().classRoomLessons(course, a, token).enqueue(new Callback<Responses<ClassRoomLessonContent>>() {
                @Override
                public void onResponse(Call<Responses<ClassRoomLessonContent>> call, retrofit2.Response<Responses<ClassRoomLessonContent>> response) {

                    ipcProgressDialog.hideIPCProgressDialog();
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
                    ipcProgressDialog.hideIPCProgressDialog();
                    Log.e("MY", "error: " + t.getMessage());
                }
            });
        }else{
            showError(getResources().getString(R.string.no_internet_text));
        }
    }

    public void editClassRoomItem(int a){
        if(CheckInternetConnection.isConnected(this)){
            ipcProgressDialog.showIPCProgressDialog();
            final String token = auth.getToken();
            String course =  IPC_Application.i().getPreferences().getSelectedCourse();
            IPC_Application.i().w().editLessons(course, a, title.getText().toString(),"This is Edited Body from our app.",null,token).enqueue(new Callback<Responses<ArrayList<EditLessonContent>>>() {
                @Override
                public void onResponse(Call<Responses<ArrayList<EditLessonContent>>> call, retrofit2.Response<Responses<ArrayList<EditLessonContent>>> response) {

                    ipcProgressDialog.hideIPCProgressDialog();
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
                    ipcProgressDialog.hideIPCProgressDialog();
                    Log.e("MY", "error: " + t.getMessage());
                }
            });

        }else {
            showError(getResources().getString(R.string.no_internet_text));
        }


    }
    @Override
    public void getDataFromServer() {

    }

    @Override
    public void sendDataToServer(String... strings) {

    }


    @Override
    public void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(EditClassRoomLessonActivity.this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }
}
