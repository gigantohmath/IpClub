package ipclub.com.ipclub;

import java.util.ArrayList;

import ipclub.com.ipclub.contents.EmptyContent;
import ipclub.com.ipclub.contents.LoginContent;
import ipclub.com.ipclub.contents.VocabularyItem;
import ipclub.com.ipclub.responses.Responses;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface I_Requests {

    static final String address = "https://api.ipc.am";

    @FormUrlEncoded
    @POST("/rest/login")
    Call<Responses<LoginContent>> login(
            @Field("login") String login,
            @Field("password") String password
    );


    @GET("/rest/refreshToken")
    Call<Responses<LoginContent>> refreshToken(
            @Header("token") String token
    );

    @GET("/rest/{course}/vocabulary/list/{start}/{count}")
    Call<Responses<ArrayList<VocabularyItem>>> vocabularyItems(
            @Path("course") String course,
            @Path("start") int start,
            @Path("count") int count,
            @Header("token") String token
    );


    @FormUrlEncoded
    @POST("/rest/changePassword")
    Call<Responses<ArrayList<EmptyContent>>> changePassword(
            @Field("password") String password,
            @Field("newpassword") String newPassword,
            @Header("token") String token
    );


}
