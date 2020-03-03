package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
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

public class WrongCodeActivity extends BaseActivity implements View.OnClickListener {

    String phone = "";

    @BindView(R.id.btn_correct)
    Button btn_correct;

    @BindView(R.id.btn_resend)
    Button btn_resend;


    public static void launch(Activity activity, String json) {
        Intent intent = new Intent(activity.getBaseContext(), WrongCodeActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_code);
        ButterKnife.bind(this);

        phone = getIntent().getStringExtra("json");


        btn_correct.setOnClickListener(this);
        btn_resend.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_resend) {
            getData(phone);
        } else if (view.getId() == R.id.btn_correct) {
            LoginActivity.launch(WrongCodeActivity.this, phone);
            exit();
        }

    }

    public void getData(final String tokenx) {
        if (NetUtil.isNetworkAvailable(getBaseContext())) {

         showPD();

            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call = ramsarfoodService.sendSms(API_BASE_URL + "api/app/sendSms", tokenx);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
//                            Log.d("okok", response.raw().toString());
                            JSONObject jsonObject = new JSONObject(response.body().string());
hidePD();                            if (jsonObject.getBoolean("success")) {
                                VerifyActivity.launch(WrongCodeActivity.this, tokenx);
                                exit();
                            }
//                            showToast(jsonObject.getString("msg"));
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
