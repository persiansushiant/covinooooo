package ir.technopedia.covino;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import ir.technopedia.covino.util.SharedPreferencesManager;

public class BaseActivity extends AppCompatActivity {

    public SharedPreferencesManager sharedPreferencesManager;
    Typeface lalezarFont;

    public String token = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void showToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        lalezarFont = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/lalezar.ttf");
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkUser();
    }

    public void checkUser() {

//        if (sharedPreferencesManager.getStringValue("user") != null
//                && !(sharedPreferencesManager.getStringValue("user").equals(""))
//                && !(sharedPreferencesManager.getStringValue("user").equals("null"))) {
//            user = new Gson().fromJson(sharedPreferencesManager.getStringValue("user"), User.class);
//        }

    }

//    public void showLoginDialog(Activity activity) {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_login);
//
//        final TextView message = (TextView) dialog.findViewById(R.id.message);
//        final TextView btn_login = (TextView) dialog.findViewById(R.id.btn_login);
//        final TextView btn_cancel = (TextView) dialog.findViewById(R.id.btn_cancel);
//        final TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
//
//        dialog_title.setText("ورود");
//        message.setText("کاربر عزیز برای استفاده از امکانات برنامه و یا دسترسی به پروفایل باید وارد برنامه شوید!");
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginActivity.launch(activity, "");
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

    public void setLalezarFont(TextView view) {
        view.setTypeface(lalezarFont);
    }

    public void exit() {
        this.finish();
    }
}
