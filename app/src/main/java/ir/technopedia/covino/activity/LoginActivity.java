package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.input_phone)
    EditText input_phone;


    public static void launch(Activity activity, String json) {
        Intent intent = new Intent(activity.getBaseContext(), LoginActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        String json = getIntent().getStringExtra("json");
        if (!json.equals("")) {
            input_phone.setText(json);
            if (json.length() == 11)
                btn_login.setBackground(getBaseContext().getResources().getDrawable(R.drawable.btn_primary));
            else
                btn_login.setBackground(getBaseContext().getResources().getDrawable(R.drawable.btn_grey));
        }

        input_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 11)
                    btn_login.setBackground(getBaseContext().getResources().getDrawable(R.drawable.btn_primary));
                else
                    btn_login.setBackground(getBaseContext().getResources().getDrawable(R.drawable.btn_grey));
            }
        });

        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            if (input_phone.getText().toString().length() == 11)
                getData(input_phone.getText().toString());
            else
                showToast("لطفا شماره خود را وارد نمایید.", 0);
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
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            hidePD();
//                            showToast(jsonObject.getString("msg"));
                            if (jsonObject.getBoolean("success")) {
                                VerifyActivity.launch(LoginActivity.this, tokenx);
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
            showToast("لطفا اینترنت گوشی خود را چک کنید!", 0);
        }
    }
}
