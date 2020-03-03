package ir.technopedia.covino;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import ir.technopedia.covino.util.SharedPreferencesManager;

public class BaseActivity extends AppCompatActivity {

    public SharedPreferencesManager sharedPreferencesManager;
    Typeface lalezarFont;
    Dialog dialog;


    public String token = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void showToast(String message,int faz) {
        if(faz==0)//warning
            Crouton.makeText(BaseActivity.this, message, Style.ALERT).show();
        else
            Crouton.makeText(BaseActivity.this, message, Style.CONFIRM).show();


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        lalezarFont = Typeface.createFromAsset(getBaseContext().getAssets(), "fonts/lalezar.ttf");
    }
    public void showPD() {

        dialog = new Dialog(BaseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.sdm_loading);

        ImageView view = (ImageView) dialog.findViewById(R.id.img);



        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

//Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(700); //Put desired duration per anim cycle here, in milliseconds

//Start animation
        view.startAnimation(anim);

        ;

        dialog.show();

    }
    public void hidePD() {
        if(dialog!=null)
            dialog.dismiss();
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
