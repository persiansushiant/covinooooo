package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseActivity;
import ir.technopedia.covino.R;
import ir.technopedia.covino.util.SharedPreferencesManager;

public class WashSuccessActivity extends BaseActivity {
    private static int counterz;
    String url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-32-98.gif";
    @BindView(R.id.img)
    ImageView img;
    SharedPreferencesManager sharedPreferencesManager;

    public static void launch(Activity activity, String json, String counter) {

        Intent intent = new Intent(activity.getBaseContext(), WashSuccessActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_success);
        ButterKnife.bind(this);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(WashSuccessActivity.this);

        counterz = Integer.parseInt(sharedPreferencesManager.getStringValue("counter"));
        calculateURl();
        Glide.with(WashSuccessActivity.this)
                .load(url)
                .asGif()

                .into(img);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                exit();
//            }
//        }, 2000);
    }

    public void calculateURl() {

        if (counterz < 3) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-33-93.gif";
            return;
        } else if (counterz < 6) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-32-98.gif";
            return;

        } else if (counterz < 12) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-32-35.gif";
            return;
        } else if (counterz < 15) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-30-43.gif";
            return;
        } else if (counterz < 19) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-28-96.gif";
            return;
        } else if (counterz <= 20) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-39-99.gif";
            return;
        }
    }
}
