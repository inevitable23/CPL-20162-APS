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
public class Emergency2Fragment extends Fragment {


    private VideoView videoView;
    public Emergency2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_emergency2, container, false);

        videoView = (VideoView)v.findViewById(R.id.frag2_video);
        MediaController mc= new MediaController(getActivity());
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.step2;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(mc);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                videoView.requestFocus();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

}
