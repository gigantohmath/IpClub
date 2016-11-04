package ipclub.com.ipclub.common;

public interface I_CommonMethodsForWorkingWithServer {

    void getDataFromServer();
    void sendDataToServer(String... strings);
    void showError(String text);
}
