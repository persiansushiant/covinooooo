package ir.technopedia.covino;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by TheLoneWolf on 20/12/2017.
 */

//@ReportsCrashes(mailTo = "m.garebaghi@gmail.com",
//        mode = ReportingInteractionMode.DIALOG,
//        resDialogText = R.string.crash_report,
//        resDialogTitle = R.string.crash_title,
//        resDialogTheme = R.style.AppCompatAlertDialogStyle,
//        resDialogIcon = R.mipmap.ic_launcher)

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        ACRA.init(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/dana.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}