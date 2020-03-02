package ir.technopedia.covino.service;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ir.technopedia.covino.util.NetUtil;
import ir.technopedia.covino.util.ServiceGenerator;
import ir.technopedia.covino.util.SharedPreferencesManager;
import ir.technopedia.covino.util.VideoShopService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.technopedia.covino.util.ServiceGenerator.API_BASE_URL;


/**
 * Created by user1 on 12/14/2016.
 */

public class FirebaseInstanceStart extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    SharedPreferencesManager sharedPreferencesManager;

    @Override
    public void onTokenRefresh() {

        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServerx(refreshedToken);

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        Intent intent = new Intent(this, FcmRegistrationJobIntentService.class);
        FcmRegistrationJobIntentService.enqueueWork(this, intent);

    }

    private void sendRegistrationToServerx(String token) {

        if (!token.equals("")) {
            if (NetUtil.isNetworkAvailable(getBaseContext())) {

                if (!sharedPreferencesManager.getStringValue("token").equals("")) {
                    VideoShopService carmanService = ServiceGenerator.createService(VideoShopService.class);
                    Call<ResponseBody> call2 = carmanService.updateFcm(
                            API_BASE_URL + "api/app/updateFcm",
                            sharedPreferencesManager.getStringValue("token"),
                            token
                    );
                    call2.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }

            }
        }

    }
}