package ipclub.com.ipclub.docsSection;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub.changePasswordSection.ChangePasswordActivity;
import ipclub.com.ipclub.common.CheckInternetConnection;
import ipclub.com.ipclub.utils.IPCProgressDialog;
import ipclub.com.ipclub.vocabularySection.Vocabulary;
import ipclub.com.ipclub.docsSection.docsItem.DocsItemActivity;
import ipclub.com.ipclub.classRoomSection.ClassRoomActivity;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Auth auth;
    DocsContent content;
    ArrayList<SectionItem> sections;
    private  NavigationView navigationView;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private SweetAlertDialog dialog;
    private IPCProgressDialog ipcProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs);
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        sections = new ArrayList<SectionItem>();

        listAdapter = new ExpandableListAdapter(this, sections);
        expListView.setAdapter(listAdapter);

        ipcProgressDialog = new IPCProgressDialog(this);
        auth = new Auth(this);
        getDataFromServer();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.docs_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent toAnotherActivity;
        if (id == R.id.nav_vocabulary) {
            toAnotherActivity = new Intent(DocsActivity.this, Vocabulary.class);
            startActivity(toAnotherActivity);
        } else if (id == R.id.nav_docs) {
            toAnotherActivity = new Intent(DocsActivity.this, DocsActivity.class);
            startActivity(toAnotherActivity);
        } else if (id == R.id.nav_classroom) {
            toAnotherActivity = new Intent(DocsActivity.this, ClassRoomActivity.class);
            startActivity(toAnotherActivity);
        } else if (id == R.id.nav_res) {
            Toast.makeText(DocsActivity.this, "Resources part is note done yet ! ! !", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_quizes) {

            Toast.makeText(DocsActivity.this, "Quizes part is note done yet ! ! !", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            toAnotherActivity = new Intent(DocsActivity.this, ChangePasswordActivity.class);
            startActivity(toAnotherActivity);
        }
        else if (id == R.id.nav_logout) {
            showLogoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showLogoutDialog(){
        dialog= new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(this.getString(R.string.log_out_dialog_title));
        dialog.setContentText(this.getString(R.string.log_out_dialog_text));
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Auth.IS_LOGGED=false;
                auth.logout();
                dialog.dismiss();
            }
        });
        dialog.showCancelButton(true);
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        });
        dialog.setCancelText(this.getString(R.string.dialog_cancel));
        dialog.show();
    }

    private void getDataFromServer() {
        if(CheckInternetConnection.isConnected(this)){
            ipcProgressDialog.showIPCProgressDialog();
            final String token = auth.getToken();
            String course =  IPC_Application.i().getPreferences().getSelectedCourse();
            IPC_Application.i().w().getDocList(course, token).enqueue(new Callback<Responses<DocsContent>>() {
                @Override
                public void onResponse(Call<Responses<DocsContent>> call, Response<Responses<DocsContent>> response) {
                    ipcProgressDialog.hideIPCProgressDialog();
                    if(response.body().status == 200){
                        content = response.body().content;
                        sections.addAll(content.sections);

                        listAdapter.notifyDataSetChanged();
                    }else{

                    }
                }

                @Override
                public void onFailure(Call<Responses<DocsContent>> call, Throwable t) {
                    ipcProgressDialog.hideIPCProgressDialog();
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

}
