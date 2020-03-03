package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class VerifyActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.chronometer)
    TextView chronometer;

    @BindView(R.id.btn_resend)
    Button btn_resend;

    @BindView(R.id.btn_correct)
    Button btn_correct;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.sms1)
    EditText sms1;

    @BindView(R.id.sms2)
    EditText sms2;

    @BindView(R.id.sms3)
    EditText sms3;

    @BindView(R.id.sms4)
    EditText sms4;

    CountDownTimer timer;

    String phone;


    public static void launch(Activity activity, String json) {
        Intent intent = new Intent(activity.getBaseContext(), VerifyActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        ButterKnife.bind(this);

        phone = getIntent().getStringExtra("json");


        btn_correct.setOnClickListener(this);
        btn_resend.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        timer = new CountDownTimer(120 * 1000, 1000) {

            @Override
            public void onTick(final long millisUntilFinished) {
                chronometer.post(new Runnable() {
                    @Override
                    public void run() {
                        if (millisUntilFinished / 1000 == 30) {
                            chronometer.setTextColor(getBaseContext().getResources().getColor(R.color.md_red_500));
                        }
                        chronometer.setText(millisUntilFinished / 1000 + "");
                    }
                });
            }

            @Override
            public void onFinish() {
//                LoginActivity.launch(VerifyActivity.this, phone);
                exit();
            }
        };
        timer.start();

        sms1.addTextChangedListener(new TextWatcher() {

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
                if (s.length() == 1)
                    sms2.requestFocus();
            }
        });

        sms2.addTextChangedListener(new TextWatcher() {

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
                if (s.length() == 1)
                    sms3.requestFocus();
            }
        });

        sms3.addTextChangedListener(new TextWatcher() {

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
                if (s.length() == 1)
                    sms4.requestFocus();
            }
        });

        sms4.addTextChangedListener(new TextWatcher() {

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
                if (s.length() == 1)
                    btn_login.setBackground(getBaseContext().getResources().getDrawable(R.drawable.btn_primary));
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_correct) {
            LoginActivity.launch(VerifyActivity.this, phone);
            exit();
        } else if (view.getId() == R.id.btn_resend) {
            getData(phone);
        } else if (view.getId() == R.id.btn_login) {
            if (!(sms1.getText().toString().equals("")) && !(sms2.getText().toString().equals("")) && !(sms3.getText().toString().equals("")) && !(sms4.getText().toString().equals(""))) {
                String sms = sms1.getText().toString() + sms2.getText().toString() + sms3.getText().toString() + sms4.getText().toString();
                validateCode(phone, sms);
            }
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
                            Log.d("okok", response.raw().toString());
                            JSONObject jsonObject = new JSONObject(response.body().string());
hidePD();
if (jsonObject.getBoolean("success")) {
                                showToast("ارسال شد",1);
                                timer.cancel();
                                timer.start();
                            }
                            showToast(jsonObject.getString("msg"),1);
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

    public void validateCode(final String phone, final String sms) {
        if (NetUtil.isNetworkAvailable(getBaseContext())) {

           hidePD();

            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call = ramsarfoodService.verifySms(API_BASE_URL + "api/app/codeValidation", phone, sms);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
hidePD();
//                            showToast(jsonObject.getString("msg"));
                            if (jsonObject.getBoolean("success")) {
                                JSONObject user = jsonObject.getJSONObject("user");
                                sharedPreferencesManager.setStringValue("user_id", user.getString("user_id"));
                                sharedPreferencesManager.setStringValue("token", user.getString("token"));
                                sharedPreferencesManager.setStringValue("phone", user.getString("phone"));
                                sharedPreferencesManager.setStringValue("messages", user.getString("messages"));
                                String dateg = user.getString("date");
                                String time = user.getString("time");
                                String last_time = "";
                                if (!dateg.equals("")) {
                                    last_time = dateg + " " + time;
                                }
                                sharedPreferencesManager.setStringValue("last_time", last_time);
                                sharedPreferencesManager.setStringValue("last_date", dateg);
                                sharedPreferencesManager.setStringValue("counter", user.getString("count"));
                                SuccessCodeActivity.launch(VerifyActivity.this, "");
                                exit();
                            } else {
                                WrongCodeActivity.launch(VerifyActivity.this, phone);
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
