package ipclub.com.ipclub._4_vocabularySection;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditVocabulary extends AppCompatActivity {

    EditText title;
    EditText trans;
    Auth auth;
    AlertDialog customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vocabulary);
        auth = new Auth(this);
        initCustomLoading();

        title = (EditText) findViewById(R.id.title);
        trans = (EditText) findViewById(R.id.trans);
    }

    public void saveVoc(View v){
        String titleStr = title.getText().toString();
        String transStr = trans.getText().toString();
        String token = auth.getToken();
        loading(true);
        IPC_Application.i().w().addVocItem(titleStr, transStr, "Android_2016_1", token).enqueue(new Callback<Responses<VocabularyItem>>() {
            @Override
            public void onResponse(Call<Responses<VocabularyItem>> call, Response<Responses<VocabularyItem>> response) {
                loading(false);
                setResult(10);
                finish();
            }

            @Override
            public void onFailure(Call<Responses<VocabularyItem>> call, Throwable t) {
                loading(false);
            }
        });
    }

    public void backToList(View v) {
        finish();
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
}
