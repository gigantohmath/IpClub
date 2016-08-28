package ipclub.com.ipclub._5_docsSection.docsItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub._2_changePasswordSection.ChangePasswordActivity;
import ipclub.com.ipclub._4_vocabularySection.Vocabulary;
import ipclub.com.ipclub._5_docsSection.DocsActivity;
import ipclub.com.ipclub._5_docsSection.DocsContent;
import ipclub.com.ipclub._6_classRoomSection.ClassRoomActivity;
import ipclub.com.ipclub._6_classRoomSection.classRoomLesson.ClassRoomLessonActivity;
import ipclub.com.ipclub._6_classRoomSection.classRoomLesson.ClassRoomLessonContent;
import ipclub.com.ipclub._6_classRoomSection.classRoomLesson.PrettifyHighlighter;
import ipclub.com.ipclub.common.Auth;
import ipclub.com.ipclub.common.IPC_Application;
import ipclub.com.ipclub.common.I_CommonMethodsForWorkingWithServer;
import ipclub.com.ipclub.common.requests.I_Requests;
import ipclub.com.ipclub.common.responses.Responses;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocsItemActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,I_CommonMethodsForWorkingWithServer{
    private AlertDialog customProgress;
    private Auth auth;
    private static PDFView pdfView;
    public final String folderDir = "IPClub";
    private static Context context;
    private SweetAlertDialog dialog;
    private  NavigationView navigationView;
    private int tempIDfromDocNavToClassRoomLesson;
    private String tempLinkFromDocNavToIntentChooser;
    private Menu menu;
    private SubMenu subMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        auth = new Auth(this);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        context =this;
        initCustomLoading();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("id",0);
        getDocExampleFromServer(intValue);


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
            toAnotherActivity = new Intent(DocsItemActivity.this, Vocabulary.class);
            startActivity(toAnotherActivity);
        } else if (id == R.id.nav_docs) {
            toAnotherActivity = new Intent(DocsItemActivity.this, DocsActivity.class);
            startActivity(toAnotherActivity);
        } else if (id == R.id.nav_classroom) {
            toAnotherActivity = new Intent(DocsItemActivity.this, ClassRoomActivity.class);
            startActivity(toAnotherActivity);
        } else if (id == R.id.nav_res) {
            Toast.makeText(DocsItemActivity.this, "Resources part is note done yet ! ! !", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_quizes) {

            Toast.makeText(DocsItemActivity.this, "Quizes part is note done yet ! ! !", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            toAnotherActivity = new Intent(DocsItemActivity.this, ChangePasswordActivity.class);
            startActivity(toAnotherActivity);
        }
        else if (id == R.id.nav_logout) {
            showLogoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void getDataFromServer() {

    }

    @Override
    public void sendDataToServer(String... strings) {

    }

    public void getDocExampleFromServer(int a){
        final int temp = a;
        loading(true);
        final String token = auth.getToken();
        Log.d("MYTOKEN",token);
        IPC_Application.i().w().getDocLesson("Android_2016_1", a, token).enqueue(new Callback<Responses<docsItemContent>>() {
            @Override
            public void onResponse(Call<Responses<docsItemContent>> call, retrofit2.Response<Responses<docsItemContent>> response) {

                loading(false);
                if(response.code() == 200){
                    if(response.body().status == 200){
                        final ArrayList<docsItemContentClassrooms> classRooms = response.body().content.classrooms;
                        ArrayList<docsItemContentFiles> files = response.body().content.files;
                        menu = navigationView.getMenu();
                        if(classRooms.size()>0){
                             subMenu = menu.addSubMenu("Classrooms");
                            for (int i = 0; i < classRooms.size(); i++) {
                                subMenu.add(classRooms.get(i).title);
                                tempIDfromDocNavToClassRoomLesson = classRooms.get(i).id;
                                subMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        Intent toDocsItemActivty = new Intent(DocsItemActivity.this, ClassRoomLessonActivity.class);
                                        toDocsItemActivty.putExtra("id",tempIDfromDocNavToClassRoomLesson);
                                        startActivity(toDocsItemActivty);
                                        return true;
                                    }
                                });
                            }

                        }


                        if(files.size()!=0){
                            subMenu = menu.addSubMenu("Files");

                            if(files.size()==1){

                                getDocExampleFileFromServer(response.body().content.files.get(0).id,0);

                            }

                            else{
                                getDocExampleFileFromServer(response.body().content.files.get(0).id,0);
                                for(int i=1;i<files.size();i++){

                                    subMenu.add("TempTitle" + i);
                                }

                                for(int i=1;i<files.size();i++){

                                    getDocExampleFileFromServer(response.body().content.files.get(i).id,i);
                                }

                            }





                        }
                    }else{
                        showError("Error: "+response.body().message);
                    }

                }else{
                    showError("Something went wrong. "+response.code());
                }

            }

            @Override
            public void onFailure(Call<Responses<docsItemContent>> call, Throwable t) {
                loading(false);
                Log.e("MY", "error: " + t.getMessage());
            }
        });


    }
    public void getDocExampleFileFromServer(int a, final int position){
        final int temp = a;
        final String token = auth.getToken();

        IPC_Application.i().w().getDocLessonFile("Android_2016_1", a,token).enqueue(new Callback<Responses<DocsItemFileContent>>() {
            @Override
            public void onResponse(Call<Responses<DocsItemFileContent>> call, Response<Responses<DocsItemFileContent>> response) {

                if(response.code() == 200){
                    if(response.body().status == 200){
                        DocsItemFileContent content = response.body().content;
                        if(position==0){

                            chooseDownloadOrOpen(response.body().content.link);
                        }

                        else{
                            tempLinkFromDocNavToIntentChooser = content.link;
                            subMenu.getItem(position-1).setTitle(content.title);
                            subMenu.getItem(position-1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {

                                    Uri webpage = Uri.parse(tempLinkFromDocNavToIntentChooser);
                                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                                    startActivity(webIntent);
                                    return true;
                                }
                            });

                        }


                    }

                    else{

                        showError("Error: "+response.body().message);
                    }

                }

                else{
                    showError("Something went wrong. "+response.code());

                }
            }

            @Override
            public void onFailure(Call<Responses<DocsItemFileContent>> call, Throwable t) {
                Log.e("MY", "error: " + t.getMessage());
            }
        });

    }

    public void chooseDownloadOrOpen(String s){
        File dir = new File(Environment.getExternalStorageDirectory(), folderDir);
        if (!dir.exists()) {
            dir.mkdir();
            File file = new File(dir, takeTheNameOfFile(s));
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            PDFDownloader.DownloadFile(I_Requests.address+s, file);
        }


        else{
            File file = new File(dir, takeTheNameOfFile(s));
            if(file.exists() && file.length()>0){

                showPDF(file);

            }

            else{

                try {
                    file.delete();
                    file.createNewFile();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                PDFDownloader.DownloadFile(I_Requests.address+s, file);

            }

        }

    }

    public static void showPDF(File f){



        pdfView.fromFile(f)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // .onDraw(onDrawListener)
                // .onLoad(onLoadCompleteListener)
                // .onPageChange(onPageChangeListener)
                // .onPageScroll(onPageScrollListener)
                // .onError(onErrorListener)
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(new DefaultScrollHandle(context))
                .load();

    }

    public String takeTheNameOfFile(String s){
        String[] ss = s.split("/");
        return  ss[ss.length-1];
    }

    @Override
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
    public void loading(boolean show) {
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
}
