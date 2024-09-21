package com.example.note.ApiService;

import com.example.note.Data.model.ResponseAvatar;
import com.example.note.Data.model.ResponseClass;
import com.example.note.Data.model.ResponseNote;
import com.example.note.Data.model.ResponseSchedule;
import com.example.note.Data.model.ResponseStatus;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("getNote.php")
    Call<ResponseNote> getNoteById(@Body Map<String, Integer> id);

    @POST("getClass.php")
    Call<ResponseClass> getClassById(@Body Map<String, Integer> id);

    @POST("getAvatarUrl.php")
    Call<ResponseAvatar> getAvatarUrl(@Body Map<String, Integer> id);

    @POST("setAvatarUrl.php")
    Call<ResponseStatus> setAvatarUrl(@Body Map<String, Integer> id);

    @POST("getCalendar.php")
    Call<ResponseSchedule> getSchedules(@Body Map<String, Integer> id);
}
