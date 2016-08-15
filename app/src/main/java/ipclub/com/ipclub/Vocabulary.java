package ipclub.com.ipclub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Vocabulary extends AppCompatActivity {

    SweetAlertDialog pLoading;
    public Auth auth;
    ArrayList<JSONObject> dataSet;
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

        dataSet = new ArrayList<JSONObject>();
        adapter = new CustomAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        initLoading();
        getDataFromServer();



    }

    private void getDataFromServer(){
        String url = "https://api.ipc.am/rest/TEST/vocabulary/list/1/100";
        final String token = auth.getToken();
        loading(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject data = new JSONObject(response);

                            if(data.getString("message").equals("Success")){

                                JSONArray content = data.getJSONArray("content");
                                for(int i = 0; i < content.length(); i++){
                                    dataSet.add(content.getJSONObject(i));
                                }
                                adapter.notifyDataSetChanged();
                                loading(false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MY", error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("token",token);
                return params;
            }
        };


        ApplicationController.getInstance().addToRequestQueue(stringRequest);
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
}
