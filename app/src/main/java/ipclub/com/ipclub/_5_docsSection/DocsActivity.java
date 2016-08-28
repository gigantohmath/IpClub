package ipclub.com.ipclub._5_docsSection;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub._5_docsSection.docsItem.DocsItemActivity;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocsActivity extends AppCompatActivity {

    public Auth auth;
    AlertDialog customProgress;

    DocsContent content;
    ArrayList<SectionItem> sections;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        sections = new ArrayList<SectionItem>();

        listAdapter = new ExpandableListAdapter(this, sections);
        expListView.setAdapter(listAdapter);

        auth = new Auth(this);
        initCustomLoading();
        getDataFromServer();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                    int tempID =sections.get(groupPosition).lessons.get(childPosition).id;
                    Intent toDocsItemActivty = new Intent(DocsActivity.this, DocsItemActivity.class);
                    toDocsItemActivty.putExtra("id",tempID);
                    startActivity(toDocsItemActivty);

                return false;
            }
        });
    }

    private void getDataFromServer() {
        loading(true);
        final String token = auth.getToken();
        loading(true);
        IPC_Application.i().w().getDocList("Android_2016_1", token).enqueue(new Callback<Responses<DocsContent>>() {
            @Override
            public void onResponse(Call<Responses<DocsContent>> call, Response<Responses<DocsContent>> response) {
                loading(false);
                if(response.body().status == 200){
                   content = response.body().content;
                   sections.addAll(content.sections);

                   listAdapter.notifyDataSetChanged();
                }else{

                }
            }

            @Override
            public void onFailure(Call<Responses<DocsContent>> call, Throwable t) {
                loading(false);
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

    private void loading(boolean show){
        if (show){
            //pLoading.show();
            customProgress.show();
        }else {
            //pLoading.hide();
            customProgress.hide();
        }
    }

    private void showRrror(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(DocsActivity.this.getString(R.string.error_dialog_title))
                .setContentText(text)
                .show();
    }
}
