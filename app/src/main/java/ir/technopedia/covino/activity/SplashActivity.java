package ir.technopedia.covino.activity;

import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import ir.technopedia.covino.BaseActivity;
import ir.technopedia.covino.R;
import ir.technopedia.covino.util.NetUtil;
import ir.technopedia.covino.util.ServiceGenerator;
import ir.technopedia.covino.util.VideoShopService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.technopedia.covino.util.ServiceGenerator.API_BASE_URL;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferencesManager.getStringValue("user_id").equals("")) {
                    LoginActivity.launch(SplashActivity.this, "");
                    exit();
                } else {
                    isLogin(sharedPreferencesManager.getStringValue("phone"), sharedPreferencesManager.getStringValue("token"));
                }
            }
        }, 1500);

    }

    public void isLogin(String phone, final String token) {
        if (NetUtil.isNetworkAvailable(getBaseContext())) {

            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call = ramsarfoodService.isLogin(API_BASE_URL + "api/app/isLogin", phone, token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getBoolean("success")) {
                                JSONObject user = jsonObject.getJSONObject("user");

                                String dateg = user.getString("date");
                                String time = user.getString("time");
                                String last_time = "";
                                if (!dateg.equals("")) {
                                    last_time = dateg + " " + time;
                                }
                                sharedPreferencesManager.setStringValue("last_time", last_time);
                                sharedPreferencesManager.setStringValue("last_date", dateg);
                                sharedPreferencesManager.setStringValue("counter", user.getString("count"));
                                sharedPreferencesManager.setStringValue("messages", user.getString("messages"));

                                MainActivity.launch(SplashActivity.this, "");
                                exit();
                            } else {
                                LoginActivity.launch(SplashActivity.this, "");
                                exit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } else {
            showToast("لطفا اینترنت گوشی خود را چک کنید!",0);
        }
    }
}
