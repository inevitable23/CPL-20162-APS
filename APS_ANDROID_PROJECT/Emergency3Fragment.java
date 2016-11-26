package resolution.ex6.vr.aps;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Emergency3Fragment extends Fragment {


    private VideoView videoView;

    public Emergency3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_emergency3, container, false);

        videoView = (VideoView)v.findViewById(R.id.frag3_video);
        MediaController mc= new MediaController(getActivity());
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.step3;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(mc);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                videoView.requestFocus();
            }
        });
        return v;
    }

}
