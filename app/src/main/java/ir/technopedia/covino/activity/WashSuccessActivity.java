package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

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
    MediaPlayer mp;
    String PATH_TO_FILE = "http://www.kamranhatami.ir/corona/";

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
        mp = new MediaPlayer();

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

    public void playMusic(int s) {
        String sz = PATH_TO_FILE + s + ".mp3";
        Log.wtf("music", sz);
        try {

            mp.setDataSource(sz);
            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    exit();
                }
            });
        } catch (IOException e) {
            Log.wtf("music", e.toString());
            e.printStackTrace();
        }
    }

    public void calculateURl() {

        if (counterz < 3) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-33-93.gif";//emsho
            playMusic(1);

            return;
        } else if (counterz < 6) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-32-98.gif";////nari nari
            playMusic(2);

            return;

        } else if (counterz < 12) {
            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-32-35.gif";//hale khubi daram
            playMusic(3);

            return;
        } else if (counterz < 15) {
            playMusic(4);

            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-30-43.gif";//age yadesh bere
            return;
        } else if (counterz < 19) {
            playMusic(5);

            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-28-96.gif";//khaste am man
            return;
        } else if (counterz <= 20) {
            playMusic(6);

            url = "https://pic.funnygifsbox.com/uploads/2019/07/funnygifsbox.com-2019-07-07-11-09-39-99.gif";//man marde tanhaye shabam
            return;
        }
    }
}
