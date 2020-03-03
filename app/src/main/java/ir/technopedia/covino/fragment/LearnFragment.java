package ir.technopedia.covino.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseFragment;
import ir.technopedia.covino.R;

public class LearnFragment extends BaseFragment {
    @BindView(R.id.webview)
    WebView webview;

    public LearnFragment() {
    }

    public static LearnFragment newInstance() {
        LearnFragment fragment = new LearnFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learn, container, false);
        ButterKnife.bind(this, v);

        String s="<video id=\"player_829881_11414983_html5_api\" class=\"vjs-tech\" tabindex=\"-1\">\n" +
                "                          <source src=\"https://cdn.yjc.ir/files/fa/news/1398/12/2/11422153_265.mp4\" type=\"video/mp4\" label=\"Original\" selected=\"\">\n" +
                "                          \n" +
                "                        </video>";
        webview.loadData(s,"text/html", "UTF-8");
//        webview.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                ((BaseActivity)getActivity()).showToast(description,0);
//            }
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });

//        webview .loadUrl("https://www.yjc.ir/fa/news/7262956/%D9%86%D8%AD%D9%88%D9%87-%D8%B5%D8%AD%DB%8C%D8%AD-%D8%B4%D8%B3%D8%AA%D9%86-%D8%AF%D8%B3%D8%AA%E2%80%8C%D9%87%D8%A7-%D8%A8%D8%B1%D8%A7%DB%8C-%D9%BE%DB%8C%D8%B4%DA%AF%DB%8C%D8%B1%DB%8C-%D8%A7%D8%B2-%D8%A7%D8%A8%D8%AA%D9%84%D8%A7-%D8%A8%D9%87-%D9%88%DB%8C%D8%B1%D9%88%D8%B3-%DA%A9%D8%B1%D9%88%D9%86%D8%A7");

        return v;
    }


}