package ipclub.com.ipclub._4_vocabularySection;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ipclub.com.ipclub.R;
import ipclub.com.ipclub._2_changePasswordSection.ChangePasswordActivity;
import ipclub.com.ipclub._5_docsSection.DocsActivity;
import ipclub.com.ipclub._6_classRoomSection.ClassRoomActivity;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.NavigationItemSelector;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditVocabulary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText title;
    EditText trans;
    Auth auth;
    AlertDialog customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vocabulary);

        NavigationView navigationView = (NavigationView) findViewById(R.id.add_vocabulary_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        NavigationItemSelector n = new NavigationItemSelector();
        n.doSelectedAction(this, id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.add_vocabulary_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
