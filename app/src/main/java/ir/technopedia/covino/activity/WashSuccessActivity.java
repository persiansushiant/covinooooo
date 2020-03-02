package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ir.technopedia.covino.BaseActivity;
import ir.technopedia.covino.R;

public class WashSuccessActivity extends BaseActivity {

    public static void launch(Activity activity, String json) {
        Intent intent = new Intent(activity.getBaseContext(), WashSuccessActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_success);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exit();
            }
        }, 2000);
    }
}
