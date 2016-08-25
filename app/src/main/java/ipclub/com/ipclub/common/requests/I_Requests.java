package ipclub.com.ipclub.common.requests;

import java.util.ArrayList;
import java.util.List;

import ipclub.com.ipclub._5_docsSection.DocsContent;
import ipclub.com.ipclub._6_classRoomSection.ClassRoomItem;
import ipclub.com.ipclub._6_classRoomSection.classRoomLesson.ClassRoomLessonContent;
import ipclub.com.ipclub._6_classRoomSection.classRoomLesson.EditLessonContent;
import ipclub.com.ipclub.common.EmptyContent;
import ipclub.com.ipclub._1_loginSection.LoginContent;
import ipclub.com.ipclub._4_vocabularySection.VocabularyItem;
import ipclub.com.ipclub.common.responses.Responses;
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
 //        /rest/TEST/classroom/list/START/COUNT

    @GET("/rest/{course}/classrooms/list/{start}/{count}")
    Call<Responses<List<ClassRoomItem>>> classRoomItems(
            @Path("course") String course,
            @Path("start") int start,
            @Path("count") int count,
            @Header("token") String token
    );
    ///rest/Android_2016_1/classrooms/get/32
    @GET("/rest/{course}/classrooms/get/{id}")
    Call<Responses<ClassRoomLessonContent>> classRoomLessons(
            @Path("course") String course,
            @Path("id") int id,
            @Header("token") String token
    );

    @GET("/rest/{course}/classrooms/delete/{id}")
    Call<Responses<ArrayList<EmptyContent>>> deleteClassRoomLessons(
            @Path("course") String course,
            @Path("id") int id,
            @Header("token") String token
    );


    @FormUrlEncoded
    @POST("/rest/{course}/classrooms/edit/{id}")
    Call<Responses<ArrayList<EditLessonContent>>> editLessons(

            @Path("course") String course,
            @Path("id") int id,
            @Field("title") String title,
            @Field("content") String content,
            @Field("lesson") Integer lesson,
            @Header("token") String token
    );

    @GET("/rest/{course}/docs")
    Call<Responses<DocsContent>> getDocList(
            @Path("course") String course,
            @Header("token") String token
    );

    @FormUrlEncoded
    @POST("/rest/{course}/vocabulary/new")
    Call<Responses<VocabularyItem>> addVocItem(
            @Field("title") String title,
            @Field("translation") String translation,
            @Path("course") String course,
            @Header("token") String token
    );
}
