package ipclub.com.ipclub.vocabularySection;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.changePasswordSection.ChangePasswordActivity;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.CheckInternetConnection;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.NavigationItemSelector;
import ipclub.com.ipclub.common.responses.Responses;
import ipclub.com.ipclub.utils.IPCProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class Vocabulary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SweetAlertDialog pLoading;
    public Auth auth;
    ArrayList<VocabularyItem> dataSet;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    VocabularyAdapter adapter;
    AlertDialog customProgress;
    private FloatingActionButton fab;

    public static int REQUEST_CODE = 1;
    public static int RESULT_REFRESH = 10;
    private IPCProgressDialog ipcProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_float_layout);
        auth = new Auth(this);
        ipcProgressDialog = new IPCProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.vocabulary_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dataSet = new ArrayList<VocabularyItem>();

        adapter = new VocabularyAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        getDataFromServer();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vocabulary.this, AddEditVocabulary.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {

            if (resultCode == RESULT_REFRESH) {
                dataSet.removeAll(dataSet);
                getDataFromServer();
            }
        }

    }

    private void getDataFromServer(){
        if(CheckInternetConnection.isConnected(this)){
            ipcProgressDialog.showIPCProgressDialog();
            final String token = auth.getToken();
            String course =  IPC_Application.i().getPreferences().getSelectedCourse();
            IPC_Application.i().w().vocabularyItems(course, 1, 9999, token).enqueue(new Callback<Responses<ArrayList<VocabularyItem>>>() {
                @Override
                public void onResponse(Call<Responses<ArrayList<VocabularyItem>>> call, retrofit2.Response<Responses<ArrayList<VocabularyItem>>> response) {

                    ipcProgressDialog.hideIPCProgressDialog();
                    if(response.code() == 200){
                        if(response.body().status == 200){
                            dataSet.addAll(response.body().content);
                            adapter.notifyDataSetChanged();
                        }else{
                            showRrror(Vocabulary.this.getString(R.string.error)+":"+response.body().message);
                        }

                    }else{
                        showRrror(Vocabulary.this.getString(R.string.something_went_wrong)+response.code());
                    }

                }

                @Override
                public void onFailure(Call<Responses<ArrayList<VocabularyItem>>> call, Throwable t) {
                    ipcProgressDialog.hideIPCProgressDialog();
                    Log.e("MY", "error: " + t.getMessage());
                }
            });
        }else{
            showError(getResources().getString(R.string.no_internet_text));
        }

    }


    public void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }

    private void initLoading(){
        pLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pLoading.setTitleText("Loading");
        pLoading.setCancelable(false);
    }

   private void showRrror(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(Vocabulary.this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        NavigationItemSelector n = new NavigationItemSelector();
        n.doSelectedAction(this, id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.vocabulary_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }
}
