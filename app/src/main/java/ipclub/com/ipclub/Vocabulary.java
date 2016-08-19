package ipclub.com.ipclub;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.contents.VocabularyItem;
import ipclub.com.ipclub.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;

public class Vocabulary extends AppCompatActivity {

    SweetAlertDialog pLoading;
    public Auth auth;
    ArrayList<VocabularyItem> dataSet;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        auth = new Auth(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dataSet = new ArrayList<VocabularyItem>();

        adapter = new CustomAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        initLoading();
        getDataFromServer();

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
                        showRrror("Error: "+response.body().message);
                    }

                }else{
                    showRrror("Something went wrong. "+response.code());
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

    private void loading(boolean show){
        if (show){
            pLoading.show();
        }else {
            pLoading.hide();
        }
    }

    private void showRrror(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }
}
