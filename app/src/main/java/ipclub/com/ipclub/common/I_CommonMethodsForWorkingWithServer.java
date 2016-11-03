package ipclub.com.ipclub.common;

/**
 * Created by sench on 8/22/2016.
 */
public interface I_CommonMethodsForWorkingWithServer {

    void getDataFromServer();
    void sendDataToServer(String... strings);
    void showError(String text);
}
