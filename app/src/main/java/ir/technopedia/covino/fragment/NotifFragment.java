package ir.technopedia.covino.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseFragment;
import ir.technopedia.covino.R;
import ir.technopedia.covino.adapter.PokeAdapter;
import ir.technopedia.covino.model.Contact;
import ir.technopedia.covino.model.Poke;
import ir.technopedia.covino.model.PokeResponse;
import ir.technopedia.covino.util.NetUtil;
import ir.technopedia.covino.util.ServiceGenerator;
import ir.technopedia.covino.util.SharedPreferencesManager;
import ir.technopedia.covino.util.VideoShopService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ir.technopedia.covino.util.ServiceGenerator.API_BASE_URL;

public class NotifFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btn_permission)
    Button btn_permission;

    @BindView(R.id.permission_layout)
    LinearLayout permission_layout;

    @BindView(R.id.data_layout)
    LinearLayout data_layout;

    @BindView(R.id.recycler)
    RecyclerView recycler;

    List<Contact> contacts = new ArrayList<>();
    List<Poke> pokes = new ArrayList<>();

    PokeAdapter adapter;

    ProgressDialog progressDialog;

    SharedPreferencesManager sharedPreferencesManager;

    public NotifFragment() {
        // Required empty public constructor
    }

    public static NotifFragment newInstance() {
        NotifFragment fragment = new NotifFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notif, container, false);
        ButterKnife.bind(this, v);

        progressDialog = new ProgressDialog(getContext());

        sharedPreferencesManager = SharedPreferencesManager.getInstance(getContext());

        contacts = new ArrayList<>();
        pokes = new ArrayList<>();
        adapter = new PokeAdapter(pokes, contacts);
        recycler.setAdapter(adapter);
        btn_permission.setOnClickListener(this);

        permission_layout.setVisibility(View.VISIBLE);
        data_layout.setVisibility(View.GONE);

        return v;
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

                        contacts = new ArrayList<>();

                        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                        while (phones.moveToNext()) {
                            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Contact contact = new Contact(name, phoneNumber, "0");
                            contacts.add(contact);

                        }
                        phones.close();

                        checkData(sharedPreferencesManager.getStringValue("token"), sharedPreferencesManager.getStringValue("phone"));


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

            progressDialog.setTitle("در حال دریافت اطلاعات مخاطبان...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

            VideoShopService ramsarfoodService = ServiceGenerator.createService(VideoShopService.class);
            Call<PokeResponse> call = ramsarfoodService.getPokes(API_BASE_URL + "api/app/getPokes", token, contactsx);
            call.enqueue(new Callback<PokeResponse>() {
                @Override
                public void onResponse(Call<PokeResponse> call, Response<PokeResponse> response) {

                    if (response.isSuccessful()) {

                        pokes = response.body().getPokes();
                        adapter.updateAdapter(pokes, contacts);

                        data_layout.setVisibility(View.VISIBLE);
                        permission_layout.setVisibility(View.GONE);

                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<PokeResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            showToast("لطفا اینترنت گوشی خود را چک کنید!");
        }
    }
}
