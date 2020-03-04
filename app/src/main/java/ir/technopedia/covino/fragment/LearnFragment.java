package ir.technopedia.covino.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ir.technopedia.covino.BaseFragment;
import ir.technopedia.covino.R;
import ir.technopedia.covino.activity.MainActivity;

public class LearnFragment extends BaseFragment {
    @BindView(R.id.videoView)
    VideoView videoView;

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
        MediaController mediaController= new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse("https://cdn.yjc.ir/files/fa/news/1398/12/2/11422153_265.mp4");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ((MainActivity)getActivity()).hidePD();
            }
        });
        videoView.requestFocus();
        videoView.start();
        ((MainActivity)getActivity()).showPD();



        return v;
    }


}