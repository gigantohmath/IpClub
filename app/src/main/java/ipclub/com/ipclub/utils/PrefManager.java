package ipclub.com.ipclub.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "ipc_pref";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String TOKEN = "token";
    private static final String TOKEN_LAST_USE_DATE = "token_date";
    private static final String SELECTED_COURSE = "selected_course";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(TOKEN, "");
    }

    public void setTokenDate(Long date) {
        editor.putLong(TOKEN_LAST_USE_DATE, date);
        editor.commit();
    }

    public Long getTokenDate() {
        return pref.getLong(TOKEN_LAST_USE_DATE, 0L);
    }

    public void setSelectedCourse(String course) {
        editor.putString(SELECTED_COURSE, course);
        editor.commit();
    }

    public String getSelectedCourse() {
        return pref.getString(SELECTED_COURSE, "");
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

}