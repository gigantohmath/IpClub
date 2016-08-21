package ipclub.com.ipclub.activities;

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
import ipclub.com.ipclub.Auth;
import ipclub.com.ipclub.IPC_Application;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.contents.ClassRoomItem;
import ipclub.com.ipclub.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;

public class ClassRoomActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ClassRoomItem> classRoomItems;
    private Auth auth;
    private AlertDialog customProgress;
    private PullToRefreshView mPullToRefreshView;
    private static int REFRESH_DELAY = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);
        mRecyclerView = (RecyclerView) findViewById(R.id.classRoomRecyclerView);
        auth = new Auth(this);
        classRoomItems = new ArrayList<ClassRoomItem>();
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // test to show that custom classroom adapter is working , we have a problem with classroom uri
        recCardTest();
        mAdapter = new classRoomAdapter(classRoomItems);
        mRecyclerView.setAdapter(mAdapter);
        //make working commented methods when the problem with uri will be solved
        //initCustomLoading();
        //getClassRoomDataFromServer();
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });

    }

    public void recCardTest(){

        classRoomItems.add(new ClassRoomItem(1,"Notification",1,"Custom Notification"));
        classRoomItems.add(new ClassRoomItem(2,"Threads",1,"Handlers"));
        classRoomItems.add(new ClassRoomItem(3,"Threads",1,"AsyncTask"));
        classRoomItems.add(new ClassRoomItem(4,"View",1,"Buttons"));
        classRoomItems.add(new ClassRoomItem(5,"View",2,"Layouts"));
        classRoomItems.add(new ClassRoomItem(6,"Activity",1,"Fragments"));
    }
    private void getClassRoomDataFromServer(){
        loading(true);
        final String token = auth.getToken();
        Log.e("MYTOKEN",token);
        IPC_Application.i().w().classRoomItems("Android_2016_1", 1, 9999, token).enqueue(new Callback<Responses<List<ClassRoomItem>>>() {
            @Override
            public void onResponse(Call<Responses<List<ClassRoomItem>>> call, retrofit2.Response<Responses<List<ClassRoomItem>>> response) {

                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        classRoomItems.addAll(response.body().content);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        showRrror("Error: "+response.body().message);
                    }

                }else{
                    showRrror("Something went wrong. "+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<List<ClassRoomItem>>> call, Throwable t) {
                loading(false);
                Log.e("MY", "error: " + t.getMessage());
            }
        });
    }
    private void initCustomLoading() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_progress, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        customProgress = dialogBuilder.create();
        customProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void showRrror(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }
    private void loading(boolean show){
        if (show){
            customProgress.show();
        }else {
            customProgress.hide();
        }
    }

    public void fromClassRoomGoToClassRoomItem(View view){

        Toast.makeText(ClassRoomActivity.this, "ClassRoom Item", Toast.LENGTH_SHORT).show();

    }
}
