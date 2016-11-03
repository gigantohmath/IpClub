package ipclub.com.ipclub.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import ipclub.com.ipclub.R;

public class IPCProgressDialog {
    private static boolean isRunning;
    private Context context;
    private AlertDialog ipcProgressDialog;

    public IPCProgressDialog(Context c){
        context = c;
        hideIPCProgressDialog();
        isRunning = false;
    }

    public void showIPCProgressDialog(){
        if(!isRunning){
            isRunning = true;
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater =  LayoutInflater.from(context);;
            View dialogView = inflater.inflate(R.layout.custom_progress, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);
            ipcProgressDialog = dialogBuilder.create();
            ipcProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ipcProgressDialog.show();
        }
    }

    public void hideIPCProgressDialog(){
        if(isRunning){
            isRunning = false;
            if(ipcProgressDialog!=null){
                ipcProgressDialog.hide();
                ipcProgressDialog.dismiss();
            }
        }
    }
}
