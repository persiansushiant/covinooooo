package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ir.technopedia.covino.BaseActivity;
import ir.technopedia.covino.R;

public class SuccessCodeActivity extends BaseActivity {

    public static void launch(Activity activity, String json) {
        Intent intent = new Intent(activity.getBaseContext(), SuccessCodeActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_code);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.launch(SuccessCodeActivity.this,"");
                exit();
            }
        },1000);
    }
}
