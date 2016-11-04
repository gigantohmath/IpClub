package ipclub.com.ipclub.vocabularySection;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.CheckInternetConnection;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.NavigationItemSelector;
import ipclub.com.ipclub.common.responses.Responses;
import ipclub.com.ipclub.utils.IPCProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditVocabulary extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText title;
    EditText trans;
    Auth auth;
    private IPCProgressDialog ipcProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_vocabulary);

        NavigationView navigationView = (NavigationView) findViewById(R.id.add_vocabulary_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ipcProgressDialog = new IPCProgressDialog(this);
        auth = new Auth(this);

        title = (EditText) findViewById(R.id.title);
        trans = (EditText) findViewById(R.id.trans);
    }

    public void saveVoc(View v){
        if(CheckInternetConnection.isConnected(this)){
            String titleStr = title.getText().toString();
            String transStr = trans.getText().toString();
            String token = auth.getToken();
            ipcProgressDialog.showIPCProgressDialog();
            String course =  IPC_Application.i().getPreferences().getSelectedCourse();
            IPC_Application.i().w().addVocItem(titleStr, transStr, course, token).enqueue(new Callback<Responses<VocabularyItem>>() {
                @Override
                public void onResponse(Call<Responses<VocabularyItem>> call, Response<Responses<VocabularyItem>> response) {
                    ipcProgressDialog.hideIPCProgressDialog();
                    setResult(10);
                    finish();
                }

                @Override
                public void onFailure(Call<Responses<VocabularyItem>> call, Throwable t) {
                    ipcProgressDialog.hideIPCProgressDialog();
                }
            });
        }else{
            showError(getResources().getString(R.string.no_internet_text));
        }

    }

    public void showError(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }

    public void backToList(View v) {
        finish();
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
