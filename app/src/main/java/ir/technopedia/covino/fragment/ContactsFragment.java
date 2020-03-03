package ir.technopedia.covino.fragment;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseActivity;
import ir.technopedia.covino.BaseFragment;
import ir.technopedia.covino.R;
import ir.technopedia.covino.adapter.ContactAdapter;
import ir.technopedia.covino.model.Contact;
import ir.technopedia.covino.util.NetUtil;
import ir.technopedia.covino.util.RecyclerItemClickListener;
import ir.technopedia.covino.util.ServiceGenerator;
import ir.technopedia.covino.util.SharedPreferencesManager;
import ir.technopedia.covino.util.VideoShopService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.technopedia.covino.util.ServiceGenerator.API_BASE_URL;

public class ContactsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btn_permission)
    Button btn_permission;

    @BindView(R.id.permission_layout)
    LinearLayout permission_layout;

    @BindView(R.id.data_layout)
    LinearLayout data_layout;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    List<Contact> contacts, contactsTemp = new ArrayList<>();
    List<String> poked = new ArrayList<>();

    ContactAdapter adapter;


    SharedPreferencesManager sharedPreferencesManager;

    public ContactsFragment() {
    }

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        ButterKnife.bind(this, v);


        sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());

        contacts = new ArrayList<>();
        adapter = new ContactAdapter(contacts, poked);
        recycler.setAdapter(adapter);
        btn_permission.setOnClickListener(this);

        permission_layout.setVisibility(View.VISIBLE);
        data_layout.setVisibility(View.GONE);

        recycler.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                view.findViewById(R.id.poke).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View viewx) {
                        String phone = contacts.get(position).getPhone();
                        phone = phone.replace(" ","");
                        phone = phone.replace("+98","");
                        phone = phone.replace("+1","");
                        phone = phone.replace("+44","");
                        String token = sharedPreferencesManager.getStringValue("token");
                        sendPoke(phone, token);
                        clickPoke(position);
                        adapter.updateAdapter(contacts, poked);
                    }
                });
            }
        }));

        return v;
    }

    public void sendPoke(String phone, String token) {
        if (NetUtil.isNetworkAvailable(getContext())) {

            ((BaseActivity) getActivity()).showPD();

            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call = ramsarfoodService.poke(API_BASE_URL + "api/app/poke", token, phone);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            ((BaseActivity) getActivity()).hidePD();
//                            if (jsonObject.getBoolean("success")) {
//                            }
                            showToast(jsonObject.getString("msg"),1);
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
            showToast("لطفا اینترنت گوشی خود را چک کنید!",0);
        }
    }

    public void clickPoke(int index) {
        for (int i = 0; i < poked.size(); i++) {
            if (i == index) {
                poked.set(i, "ok");
            } else {
                poked.set(i, "");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getPermission();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_permission: {
                getPermission();
            }
            break;
        }
    }

    private void getPermission() {
        Dexter.withActivity(getActivity())

                .withPermission(Manifest.permission.READ_CONTACTS)

                .withListener(new PermissionListener() {

                    @Override

                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        permission_layout.setVisibility(View.GONE);

                        JSONArray jsonArray = new JSONArray();

                        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                        while (phones.moveToNext()) {
                            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Contact contact = new Contact(name, phoneNumber, "0");
                            contactsTemp.add(contact);
                            jsonArray.put(phoneNumber);

                        }
                        phones.close();

                        checkData(sharedPreferencesManager.getStringValue("token"), jsonArray.toString());


                    }

                    @Override

                    public void onPermissionDenied(PermissionDeniedResponse response) {


                        if (response.isPermanentlyDenied()) {

                        }

                    }

                    @Override

                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();

                    }

                }).check();
    }

    public void checkData(String token, final String contactsx) {

        if (NetUtil.isNetworkAvailable(getContext())) {

            ((BaseActivity) getActivity()).showPD();


            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<ResponseBody> call = ramsarfoodService.getWashs(API_BASE_URL + "api/app/getWashs", token, contactsx);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.isSuccessful()) {

                        contacts = new ArrayList<>();
                        poked = new ArrayList<>();
                        ((BaseActivity) getActivity()).hidePD();

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            ((BaseActivity) getActivity()).hidePD();
                            if (jsonObject.getBoolean("success")) {

                                JSONArray jsonArray = new JSONArray(jsonObject.getString("others"));

//                                Log.d("array", jsonArray.toString());

                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jj = jsonArray.getJSONObject(i);
                                        String phone = jj.getString("phone");
                                        String wash_count = jj.getString("wash_count");

                                        for (int j = 0; j < contactsTemp.size(); j++) {
                                            Contact cc = contactsTemp.get(j);
//                                            Log.d("cc", cc.getPhone());
//                                            Log.d("cc", cc.getName());
                                            if (cc.getPhone().equals(phone)) {
                                                boolean found = false;

                                                for (int x = 0; x < contacts.size(); x++) {
                                                    String phonex = cc.getPhone();
                                                    phonex = phonex.replace(" ", "");
                                                    String phonexx = contacts.get(x).getPhone();
                                                    phonexx = phonexx.replace(" ", "");
                                                    if (phonex.equals(phonexx)) {
                                                        found = true;
                                                    }
                                                }

                                                if (!found) {
                                                    cc.setCount(wash_count);
                                                    contacts.add(cc);
                                                    poked.add("");
                                                }

                                                break;
                                            }
                                        }
                                    }

                                    data_layout.setVisibility(View.VISIBLE);
                                    permission_layout.setVisibility(View.GONE);

                                    adapter.updateAdapter(contacts, poked);
                                }

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
            showToast("لطفا اینترنت گوشی خود را چک کنید!",0);
        }
    }
}
