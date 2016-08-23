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
                        builder.setTitle("What to do ?")
                                .setMessage("Delete or Edit the lesson. For only watching just click the lesson")
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        int tempId = classRoomItems.get(position).id;
                                        deleteLessonFromClassRoom(tempId);
                                    }
                                })
                                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent tempIntent = new Intent(context,EditClassRoomLessonActivity.class);
                                        int tempId = classRoomItems.get(position).id;
                                        tempIntent.putExtra("id",tempId);
                                        startActivity(tempIntent);
                                    }
                                })
                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Deleted . . .", Toast.LENGTH_SHORT).show();
                        getDataFromServer();
                    }else{
                        showError("Error: "+response.body().message);
                    }

                }else{
                    showError("Something went wrong. "+response.code());
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
                        showError("Error: "+response.body().message);
                    }

                }else{
                    showError("Something went wrong. "+response.code());
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
                .setTitleText("Oops...")
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
