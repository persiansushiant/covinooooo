package ir.technopedia.covino.util;

import ir.technopedia.covino.model.PokeResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface VideoShopService {

//    @Headers("User-Agent: android_app")
//    @FormUrlEncoded
//    @POST
//    Call<MainModel> getData(
//            @Url String url,
//            @Field("token") String token
//    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<ResponseBody> sendSms(
            @Url String url,
            @Field("phone") String phone
    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<ResponseBody> verifySms(
            @Url String url,
            @Field("phone") String phone,
            @Field("sms") String sms
    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<ResponseBody> isLogin(
            @Url String url,
            @Field("phone") String phone,
            @Field("token") String token
    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<ResponseBody> wash(
            @Url String url,
            @Field("token") String token
    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<ResponseBody> getWashs(
            @Url String url,
            @Field("token") String token,
            @Field("contacts") String contacts
    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<ResponseBody> poke(
            @Url String url,
            @Field("token") String token,
            @Field("phone") String phone
    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<PokeResponse> getPokes(
            @Url String url,
            @Field("token") String token,
            @Field("phone") String phone
    );

    @Headers("User-Agent: android_app")
    @FormUrlEncoded
    @POST
    Call<ResponseBody> updateFcm(
            @Url String url,
            @Field("token") String token,
            @Field("fcm_token") String fcm_token
    );
}