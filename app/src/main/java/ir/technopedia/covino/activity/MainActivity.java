package ir.technopedia.covino.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseActivity;
import ir.technopedia.covino.R;
import ir.technopedia.covino.fragment.AchievmentFragment;
import ir.technopedia.covino.fragment.ContactsFragment;
import ir.technopedia.covino.fragment.LearnFragment;
import ir.technopedia.covino.fragment.NotifFragment;
import ir.technopedia.covino.fragment.WashFragment;
import ir.technopedia.covino.util.NetUtil;
import ir.technopedia.covino.util.ServiceGenerator;
import ir.technopedia.covino.util.VideoShopService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.technopedia.covino.util.ServiceGenerator.API_BASE_URL;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btn_exit)
    LinearLayout btn_exit;

    @BindView(R.id.btn_notifs)
    RelativeLayout btn_notifs;
    @BindView(R.id.notebook_icon)
    ImageView notebook_icon;
    @BindView(R.id.btn_contacts)
    LinearLayout btn_contacts;

    @BindView(R.id.btn_washs)
    LinearLayout btn_washs;

    @BindView(R.id.icon_notifs)
    ImageView icon_notifs;

    @BindView(R.id.icon_contacts)
    ImageView icon_contacts;

    @BindView(R.id.icon_washs)
    ImageView icon_washs;
    @BindView(R.id.achieve_icon)
    ImageView achieve_icon;
    @BindView(R.id.messages)
    TextView messages;

    Boolean isFirstPage = true, isExitable = false;

    WashFragment washFragment;

    ContactsFragment contactsFragment;

    NotifFragment notifFragment;
    LearnFragment learnFragment;
    AchievmentFragment achievmentFragment;

    public static void launch(Activity activity, String json) {
        Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
        intent.putExtra("json", json);
        activity.startActivity(intent);
    }

    public void showFragment(Fragment fragment) {
        if (fragment == washFragment) {
            isFirstPage = true;
        } else {
            isFirstPage = false;
        }
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btn_exit.setOnClickListener(this);
        btn_notifs.setOnClickListener(this);
        btn_contacts.setOnClickListener(this);
        btn_washs.setOnClickListener(this);
        notebook_icon.setOnClickListener(this);
        achieve_icon.setOnClickListener(this);
        washFragment = WashFragment.newInstance();
        contactsFragment = ContactsFragment.newInstance();
        notifFragment = NotifFragment.newInstance();
        learnFragment=LearnFragment.newInstance();
        achievmentFragment=AchievmentFragment.newInstance();

        if (!sharedPreferencesManager.getStringValue("messages").equals("")) {
            messages.setText(sharedPreferencesManager.getStringValue("messages"));
            messages.setVisibility(View.VISIBLE);
        } else {
            messages.setVisibility(View.GONE);
        }

        showFragment(washFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();


        loadFcm();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_exit: {
//                sharedPreferencesManager.setStringValue("user_id", "");
//                sharedPreferencesManager.setStringValue("token", "");
//                sharedPreferencesManager.setStringValue("phone", "");
//                sharedPreferencesManager.setStringValue("messages", "");
//                sharedPreferencesManager.setStringValue("last_time", "");
//                sharedPreferencesManager.setStringValue("last_date", "");
//                sharedPreferencesManager.setStringValue("counter", "");
//                LoginActivity.launch(MainActivity.this, "");
                exit();
            }
            break;
            case R.id.btn_notifs: {
                resetIcons();
                Glide.with(this).load(R.drawable.notifs).crossFade().into(icon_notifs);
                showFragment(notifFragment);
                messages.setVisibility(View.GONE);
            }
            break;
            case R.id.btn_contacts: {
                resetIcons();
                Glide.with(this).load(R.drawable.contacts).crossFade().into(icon_contacts);
                showFragment(contactsFragment);
            }
            break;
            case R.id.btn_washs: {
                resetIcons();
                Glide.with(this).load(R.drawable.washs).crossFade().into(icon_washs);
                showFragment(washFragment);
            }
            break;
            case R.id.notebook_icon:

                resetIcons();
                Glide.with(this).load(R.drawable.notebook).crossFade().into(notebook_icon);
                showFragment(learnFragment);

                break;
            case R.id.achieve_icon:

                resetIcons();
                Glide.with(this).load(R.drawable.achieve).crossFade().into(achieve_icon);
                showFragment(achievmentFragment);

                break;
        }

    }


    @Override
    public void onBackPressed() {
        if (isFirstPage) {
            if (isExitable) {
                super.onBackPressed();
            } else {
                showToast("برای خروج دوباره کلیک کنید", 1);
                isExitable = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitable = false;
                    }
                }, 2000);
            }
        } else {
            resetIcons();
            Glide.with(this).load(R.drawable.washs).crossFade().into(icon_washs);
            showFragment(washFragment);
        }
    }

    private void resetIcons() {
        Glide.with(this).load(R.drawable.notifs2).crossFade().into(icon_notifs);
        Glide.with(this).load(R.drawable.contacts2).crossFade().into(icon_contacts);
        Glide.with(this).load(R.drawable.washs2).crossFade().into(icon_washs);
        Glide.with(this).load(R.drawable.notebook).crossFade().into(notebook_icon);
        Glide.with(this).load(R.drawable.achieve).crossFade().into(achieve_icon);
    }

    public void loadFcm() {

        String fcmToken = FirebaseInstanceId.getInstance().getToken();

        Log.wtf("ok", fcmToken);
        if (NetUtil.isNetworkAvailable(getBaseContext())) {

            VideoShopService carmanService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call2 = carmanService.updateFcm(
                    API_BASE_URL + "api/app/updateFcm",
                    sharedPreferencesManager.getStringValue("token"),
                    fcmToken
            );
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

//                    Log.d("ok", response.raw().toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
