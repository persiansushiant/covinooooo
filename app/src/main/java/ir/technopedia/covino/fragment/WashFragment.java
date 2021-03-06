package ir.technopedia.covino.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import ir.technopedia.covino.BaseActivity;
import ir.technopedia.covino.BaseFragment;
import ir.technopedia.covino.R;
import ir.technopedia.covino.activity.WashSuccessActivity;
import ir.technopedia.covino.util.NetUtil;
import ir.technopedia.covino.util.ServiceGenerator;
import ir.technopedia.covino.util.SharedPreferencesManager;
import ir.technopedia.covino.util.VideoShopService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

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
    public static final int minLimit = 1;
    int washLimit = 20;


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

                    if (diff <= minLimit * 60 * 1000) {
                        btn_wash.setAlpha(.5f);

                        timer.setTextColor(getActivity().getResources().getColor(R.color.md_red_400));
                        btn_wash.setClickable(false);
                    } else {
                        btn_wash.setAlpha(1f);
                        timer.setTextColor(getActivity().getResources().getColor(R.color.md_black_1000));

                        btn_wash.setClickable(true);
                    }
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

        Log.wtf("1     ---->", sharedPreferencesManager.getStringValue("2"));
        String date = sharedPreferencesManager.getStringValue("last_date");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(btn_wash)
                .setDismissText("متوجه شدم")
                .setContentText("هروقت دستاتو میشوری این دکمه رو بزن")
                .setDelay(1500) // optional but starting animations immediately in onCreate can make them choppy
                .singleUse("AS") // provide a unique ID used to ensure it is only shown once
                .show();
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
                    washMyHand(sharedPreferencesManager.getStringValue("token"), sharedPreferencesManager.getStringValue("counter"));
                } else {
                    if (sharedPreferencesManager.getStringValue("counter").equals("20")) {
                        showToast("امروز به حد آخر شست شوی دستان خود رسیده اید.", 1);

                    } else {
                        washMyHand(sharedPreferencesManager.getStringValue("token"), sharedPreferencesManager.getStringValue("token"));
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

    public void washMyHand(String token, final String counterr) {

        if (NetUtil.isNetworkAvailable(getContext())) {

            ((BaseActivity) getActivity()).showPD();


            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call = ramsarfoodService.wash(API_BASE_URL + "api/app/wash", token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            ((BaseActivity) getActivity()).hidePD();
                            if (jsonObject.getBoolean("success")) {
                                String countx = jsonObject.getString("count");
                                String dateg = jsonObject.getString("dateg");
                                String time = jsonObject.getString("time");
                                CalculateBage(countx);
                                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                sharedPreferencesManager.setStringValue("last_time", dateg + " " + time);
                                sharedPreferencesManager.setStringValue("last_date", dateg);
                                sharedPreferencesManager.setStringValue("counter", countx);
                                counter.setText(countx + " / " + washLimit);
                                startRepeatingTask();
                                WashSuccessActivity.launch(getActivity(), "", counterr);
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

    private void CalculateBage(String countx) {


        if (countx.equalsIgnoreCase("5")) {
            sharedPreferencesManager.setStringValue("1", "1");
            showToast(getActivity().getResources().getString(R.string.congrats), 1);

        } else if (countx.equalsIgnoreCase("10")) {
            sharedPreferencesManager.setStringValue("2", "1");
            showToast(getActivity().getResources().getString(R.string.congrats), 1);


        } else if (countx.equalsIgnoreCase("20")) {
            sharedPreferencesManager.setStringValue("3", "1");

CalculateNewBadge();
            showToast(getActivity().getResources().getString(R.string.congrats), 1);

        }
    }

    private void CalculateNewBadge() {
        String s=sharedPreferencesManager.getStringValue("NumCompleted");
        int ss=0;
        try{
             ss=Integer.parseInt(s)+1;

        }catch (Exception e){
            ss=1;
        }
        sharedPreferencesManager.setStringValue("NumCompleted",ss+"");

        if(ss==1){
            sharedPreferencesManager.setStringValue("9", "1");
        }else if(ss==3){
            sharedPreferencesManager.setStringValue("10", "1");
            showToast(getActivity().getResources().getString(R.string.congrats), 1);
        }else if(ss==5){
            sharedPreferencesManager.setStringValue("11", "1");
            showToast(getActivity().getResources().getString(R.string.congrats), 1);
        }


    }


}
