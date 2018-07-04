package com.example.diego.handwritingnotes.utils;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by diego on 03-07-2018.
 */

public interface ImageService {
    @Headers({
            "Ocp-Apim-Subscription-Key: 93b117483ad447c4bf14969976c7a7d1",
            "Content-Type: application/octet-stream"
    })
    @Multipart
    @POST("/")
    Call<okhttp3.ResponseBody> postImage(@Part MultipartBody.Part image);
}
