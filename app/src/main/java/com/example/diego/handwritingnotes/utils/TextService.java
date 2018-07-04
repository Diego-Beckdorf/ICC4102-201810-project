package com.example.diego.handwritingnotes.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Url;

/**
 * Created by diego on 03-07-2018.
 */

public interface TextService {
    @Headers("Ocp-Apim-Subscription-Key: 93b117483ad447c4bf14969976c7a7d1")
    @GET
    Call<ResponseBody> getImageText(@Url String url);
}
