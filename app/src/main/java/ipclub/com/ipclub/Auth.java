package ipclub.com.ipclub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Artak on 8/14/2016.
 */
public class Auth {

    public Context context;
    public Activity activity;
    private final String url ="https://api.ipc.am/rest/login";
    SweetAlertDialog pLoading;

    public  static final String TOKEN_PREFERENCE = "access_token";
    public  static final String TOKEN = "token";
    public  SharedPreferences sharedpref;

    public Auth(Context context) {
        this.context = context;
        activity = (Activity) context;
        initLoading();
        sharedpref = context.getSharedPreferences(TOKEN_PREFERENCE, context.MODE_PRIVATE);
    }

    public void login(final String username, final String password){
        loading(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading(false);
                        try {
                            JSONObject data = new JSONObject(response);

                            if(data.getString("message").equals("Success")){

                                JSONObject content = data.getJSONObject("content");
                                String token = content.getString("token");

                                SharedPreferences.Editor editor = sharedpref.edit();
                                editor.putString(TOKEN, token);
                                editor.commit();

                                Intent show = new Intent(context, Dashboard.class);
                                context.startActivity(show);
                                activity.finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading(false);
                if(error.networkResponse == null){
                    showRrror("Where is your internet?");
                }
                else if(error.networkResponse.statusCode == 401){
                    showRrror("Wrong username and/or password.");
                }else{
                    showRrror("Something went wrong.");
                }

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("login", username);
                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };


        ApplicationController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showRrror(String text) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(text)
                .show();
    }

    public boolean checkLoggedIn(){
        String token = sharedpref.getString(TOKEN, "");
        Log.e("MYT", token+"");
        if(token.equals("")){
            return false;
        }
        return true;
    }

    public void logout(){
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.clear();
        editor.commit();

        Intent show = new Intent(context, MainActivity.class);
        context.startActivity(show);
    }

    @Nullable
    public String getToken(){
        String token = sharedpref.getString(TOKEN, "");
        if(token.equals("")){
            return null;
        }

        return token;
    }

    private void initLoading(){
        pLoading = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
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
