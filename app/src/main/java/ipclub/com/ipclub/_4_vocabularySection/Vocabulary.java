package ipclub.com.ipclub._4_vocabularySection;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;

public class Vocabulary extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_float_layout);
        auth = new Auth(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dataSet = new ArrayList<VocabularyItem>();

        adapter = new VocabularyAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        initCustomLoading();
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
        loading(true);
        final String token = auth.getToken();
        IPC_Application.i().w().vocabularyItems("Android_2016_1", 1, 9999, token).enqueue(new Callback<Responses<ArrayList<VocabularyItem>>>() {
            @Override
            public void onResponse(Call<Responses<ArrayList<VocabularyItem>>> call, retrofit2.Response<Responses<ArrayList<VocabularyItem>>> response) {

                loading(false);
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
                loading(false);
                Log.e("MY", "error: " + t.getMessage());
            }
        });
    }

    private void initLoading(){
        pLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pLoading.setTitleText("Loading");
        pLoading.setCancelable(false);
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

    private void loading(boolean show){
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

    private void showRrror(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(Vocabulary.this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }
}
