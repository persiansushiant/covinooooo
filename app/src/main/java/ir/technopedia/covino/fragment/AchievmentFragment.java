package ir.technopedia.covino.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseFragment;
import ir.technopedia.covino.R;
import ir.technopedia.covino.adapter.AchivementAdapter;
import ir.technopedia.covino.model.Badge;
import ir.technopedia.covino.util.SharedPreferencesManager;

public class AchievmentFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    ArrayList<Badge> myArr = new ArrayList<>();
    SharedPreferencesManager sharedPreferencesManager;
    View rootview;


    public AchievmentFragment() {
    }
    public static AchievmentFragment newInstance() {
        AchievmentFragment fragment = new AchievmentFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootview == null) {
            rootview= inflater.inflate(R.layout.fragment_achievment, container, false);
            ButterKnife.bind(this, rootview);
            sharedPreferencesManager = SharedPreferencesManager.getInstance(getActivity());
            populateData();
            FillRecycler();
            Log.wtf("here", "here");
        }else{

        }

        return rootview;
    }
    @Override
    public void onDestroyView() {
        if (rootview.getParent() != null) {
            ((ViewGroup)rootview.getParent()).removeView(rootview);
        }
        super.onDestroyView();
    }

    private void FillRecycler() {
        AchivementAdapter achivementAdapter=new AchivementAdapter(myArr,getActivity());
recycler.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recycler.setAdapter(achivementAdapter);
    }

    private void populateData() {

        if (sharedPreferencesManager.getBooleanValue("hasfilled") == false) {
            sharedPreferencesManager.setStringValue("1","0");
            sharedPreferencesManager.setStringValue("2","0");
            sharedPreferencesManager.setStringValue("3","0");
            sharedPreferencesManager.setStringValue("5","0");
            sharedPreferencesManager.setStringValue("6","0");
            sharedPreferencesManager.setStringValue("7","0");
            sharedPreferencesManager.setStringValue("9","0");
            sharedPreferencesManager.setStringValue("10","0");
            sharedPreferencesManager.setStringValue("11","0");


            sharedPreferencesManager.setBooleanValue("hasfilled", true);
        }

            myArr.add(new Badge(1, "a", "با شستن 5 بار دستانتان به این نشان می رسید.",sharedPreferencesManager.getStringValue("1")));
            myArr.add(new Badge(2, "b", "با شستن 10 بار دستانتان به این نشان می رسید", sharedPreferencesManager.getStringValue("2")));
            myArr.add(new Badge(3, "c", "با شستن 20 بار دستانتان به این نشان می رسید", sharedPreferencesManager.getStringValue("3")));
            myArr.add(new Badge(5, "e", "با شستن 100 بار دستانتان به این نشان می رسید", sharedPreferencesManager.getStringValue("5")));
            myArr.add(new Badge(6, "f", "با رسیدن به ماموریت یک روز به این نشان می رسید", sharedPreferencesManager.getStringValue("6")));
            myArr.add(new Badge(7, "g", "با کامل کردن ماموریت 3 روز به این نشان می رسید", sharedPreferencesManager.getStringValue("7")));
            myArr.add(new Badge(9, "i", "با تلنگر خوردن از 10 دوست به این نشان می رسید", sharedPreferencesManager.getStringValue("9")));
            myArr.add(new Badge(10, "j", "با تلنگر خوردن از 20 دوست به این نشان می رسید", sharedPreferencesManager.getStringValue("10")));
            myArr.add(new Badge(11, "k", "با تلنگر خوردن از 30 دوست به این نشان می رسید", sharedPreferencesManager.getStringValue("11")));

        Log.wtf("size",myArr.size()+"");
    }


}
