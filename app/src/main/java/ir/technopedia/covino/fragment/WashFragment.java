package ir.technopedia.covino.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseFragment;
import ir.technopedia.covino.R;
import ir.technopedia.covino.activity.SuccessCodeActivity;
import ir.technopedia.covino.activity.WashSuccessActivity;
import ir.technopedia.covino.util.NetUtil;
import ir.technopedia.covino.util.ServiceGenerator;
import ir.technopedia.covino.util.SharedPreferencesManager;
import ir.technopedia.covino.util.VideoShopService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.technopedia.covino.util.ServiceGenerator.API_BASE_URL;

public class WashFragment extends BaseFragment {

    @BindView(R.id.timer)
    TextView timer;

    @BindView(R.id.counter)
    TextView counter;

    @BindView(R.id.btn_wash)
    Button btn_wash;

    private int mInterval = 1000;
    private Handler mHandler;

    int washLimit = 45;

    ProgressDialog progressDialog;

    SharedPreferencesManager sharedPreferencesManager;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                String lastDateTime = sharedPreferencesManager.getStringValue("last_time");
                if (lastDateTime.equals("")) {
                    timer.setText("00:00:00");
                    stopRepeatingTask();
                } else {
                    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

                    Date d1 = null;
                    Date d2 = null;
                    try {
                        d1 = format.parse(lastDateTime);
                        d2 = format.parse(currentDate + " " + currentTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long diff = d2.getTime() - d1.getTime();
                    long diffSeconds = (diff / 1000) % 60;
                    long diffMinutes = (diff / (60 * 1000)) % 60;
                    long diffHours = (diff / (60 * 60 * 1000)) % 24;

                    timer.setText(diffHours + ":" + diffMinutes + ":" + diffSeconds);

                }
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    public WashFragment() {
    }

    public static WashFragment newInstance() {
        WashFragment fragment = new WashFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wash, container, false);
        ButterKnife.bind(this, v);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());

        progressDialog = new ProgressDialog(getContext());

        String date = sharedPreferencesManager.getStringValue("last_date");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        if (date.equals("")) {
            sharedPreferencesManager.setStringValue("last_date", currentDate);
            sharedPreferencesManager.setStringValue("counter", "0");
            counter.setText("0 / " + washLimit);
        } else {
            if (currentDate.equals(date)) {
                String washed = sharedPreferencesManager.getStringValue("counter");
                counter.setText(washed + " / " + washLimit);
            } else {
                sharedPreferencesManager.setStringValue("last_date", currentDate);
                sharedPreferencesManager.setStringValue("counter", "0");
                counter.setText("0 / " + washLimit);
            }
        }

        btn_wash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferencesManager.getStringValue("counter").equals("")) {
                    washMyHand(sharedPreferencesManager.getStringValue("token"));
                } else {
                    if (sharedPreferencesManager.getStringValue("counter").equals("45")) {
                        showToast("امروز به حد آخر شست شوی دستان خود رسیده اید.");
                    } else {
                        washMyHand(sharedPreferencesManager.getStringValue("token"));
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRepeatingTask();
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void washMyHand(String token) {
        if (NetUtil.isNetworkAvailable(getContext())) {

            progressDialog.setTitle("در حال انجام عملیات ورود...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call = ramsarfoodService.wash(API_BASE_URL + "api/app/wash", token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            progressDialog.dismiss();
                            if (jsonObject.getBoolean("success")) {
                                String countx = jsonObject.getString("count");
                                String dateg = jsonObject.getString("dateg");
                                String time = jsonObject.getString("time");

                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                sharedPreferencesManager.setStringValue("last_time", dateg + " " + time);
                                sharedPreferencesManager.setStringValue("last_date", dateg);
                                sharedPreferencesManager.setStringValue("counter", countx);
                                counter.setText(countx + " / " + washLimit);
                                startRepeatingTask();
                                WashSuccessActivity.launch(getActivity(), "");
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
            showToast("لطفا اینترنت گوشی خود را چک کنید!");
        }
    }
}
