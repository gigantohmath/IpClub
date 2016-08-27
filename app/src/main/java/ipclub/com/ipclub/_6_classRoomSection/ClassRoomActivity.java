package ipclub.com.ipclub._6_classRoomSection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub._6_classRoomSection.classRoomLesson.ClassRoomLessonActivity;
import ipclub.com.ipclub._6_classRoomSection.classRoomLesson.EditClassRoomLessonActivity;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.EmptyContent;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;

public class ClassRoomActivity extends AppCompatActivity implements I_CommonMethodsForWorkingWithServer {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ClassRoomItem> classRoomItems;
    private Auth auth;
    private AlertDialog customProgress;
    private PullToRefreshView mPullToRefreshView;
    private static int REFRESH_DELAY = 1500;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        mRecyclerView = (RecyclerView) findViewById(R.id.classRoomRecyclerView);
        auth = new Auth(this);
        context = this;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.classRoomRecyclerView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(context,ClassRoomLessonActivity.class);
                        int tempId = classRoomItems.get(position).id;
                        i.putExtra("id",tempId);
                        startActivity(i);
                    }

                    @Override public void onLongItemClick(View view, final int position) {
                        AlertDialog.Builder builder = new  AlertDialog.Builder(context);
                        builder.setTitle(ClassRoomActivity.this.getString(R.string.what_to_do))
                                .setMessage(ClassRoomActivity.this.getString(R.string.delete_or_edit_the_lesson))
                                .setNegativeButton(ClassRoomActivity.this.getString(R.string.delete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int tempId = classRoomItems.get(position).id;
                                        deleteLessonFromClassRoom(tempId);
                                    }
                                })
                                .setPositiveButton(ClassRoomActivity.this.getString(R.string.edit), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent tempIntent = new Intent(context,EditClassRoomLessonActivity.class);
                                        int tempId = classRoomItems.get(position).id;
                                        tempIntent.putExtra("id",tempId);
                                        startActivity(tempIntent);
                                    }
                                })
                                .setNeutralButton(ClassRoomActivity.this.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, ClassRoomActivity.this.getString(R.string.dialog_cancel), Toast.LENGTH_SHORT).show();
                                    }
                                });
                        builder.show();
                    }
                })
        );

        initCustomLoading();
        getDataFromServer();
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataFromServer();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

    }

    public   void  deleteLessonFromClassRoom(int id){
        loading(true);
        final String token = auth.getToken();
        IPC_Application.i().w().deleteClassRoomLessons("Android_2016_1", id, token).enqueue(new Callback<Responses<ArrayList<EmptyContent>>>() {
            @Override
            public void onResponse(Call<Responses<ArrayList<EmptyContent>>> call, retrofit2.Response<Responses<ArrayList<EmptyContent>>> response) {
                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        Toast.makeText(context, ClassRoomActivity.this.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                        getDataFromServer();
                    }else{
                        showError(ClassRoomActivity.this.getString(R.string.error)+":"+response.body().message);
                    }

                }else{
                    showError(ClassRoomActivity.this.getString(R.string.something_went_wrong)+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<ArrayList<EmptyContent>>> call, Throwable t) {
                loading(false);
                Log.e("MY", "error: " + t.getMessage());
            }
        });



    }

    @Override
    public void getDataFromServer(){
        loading(true);
        final String token = auth.getToken();
        Log.e("MYTOKEN",token);
        IPC_Application.i().w().classRoomItems("Android_2016_1", 1, 9999, token).enqueue(new Callback<Responses<List<ClassRoomItem>>>() {
            @Override
            public void onResponse(Call<Responses<List<ClassRoomItem>>> call, retrofit2.Response<Responses<List<ClassRoomItem>>> response) {

                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        classRoomItems = new ArrayList<ClassRoomItem>();
                        classRoomItems.addAll(response.body().content);
                        mAdapter = new ClassRoomAdapter(classRoomItems);
                        mRecyclerView.setAdapter(mAdapter);
                    }else{
                        showError(ClassRoomActivity.this.getString(R.string.error)+":"+response.body().message);
                    }

                }else{
                    showError(ClassRoomActivity.this.getString(R.string.something_went_wrong)+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<List<ClassRoomItem>>> call, Throwable t) {
                loading(false);
                Log.e("MY", "error: " + t.getMessage());
            }
        });
    }

    @Override
    public void sendDataToServer(String... strings) {

    }

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
                .setTitleText(ClassRoomActivity.this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }
    @Override
    public void loading(boolean show){
        if (show){
            customProgress.show();
        }else {
            customProgress.hide();
        }
    }

}
