package ipclub.com.ipclub.common;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ipclub.com.ipclub.R;
import ipclub.com.ipclub._2_changePasswordSection.ChangePasswordActivity;
import ipclub.com.ipclub._4_vocabularySection.Vocabulary;
import ipclub.com.ipclub._5_docsSection.DocsActivity;
import ipclub.com.ipclub._6_classRoomSection.ClassRoomActivity;

/**
 * Created by User on 11.09.2016.
 */
public class NavigationItemSelector {
    private SweetAlertDialog dialog;
    private Context c;
    public void doSelectedAction(Context context, int id) {
        Intent intent;
        c = context;
        if (id == R.id.nav_vocabulary) {
            intent = new Intent(context, Vocabulary.class);
            context.startActivity(intent);
        } else if (id == R.id.nav_docs) {
            intent = new Intent(context, DocsActivity.class);
            context.startActivity(intent);
        } else if (id == R.id.nav_classroom) {
            intent = new Intent(context, ClassRoomActivity.class);
            context.startActivity(intent);
        } else if (id == R.id.nav_res) {

        } else if (id == R.id.nav_quizes) {

        } else if (id == R.id.nav_settings) {
            intent = new Intent(context, ChangePasswordActivity.class);
            context.startActivity(intent);
        } else if (id == R.id.nav_logout) {
            showLogoutDialog();
        }
    }
    public void showLogoutDialog(){
        dialog= new SweetAlertDialog(c,SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(c.getString(R.string.log_out_dialog_title));
        dialog.setContentText(c.getString(R.string.log_out_dialog_text));
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Auth.IS_LOGGED=false;
                Auth auth = new Auth(c);
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
        dialog.setCancelText(c.getString(R.string.dialog_cancel));
        dialog.show();
    }
}
