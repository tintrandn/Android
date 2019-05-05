package tintrandn.co.jp.moviestore.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import tintrandn.co.jp.moviestore.R;


public class FragmentAbout extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        WebView about_web_view = (WebView) v.findViewById(R.id.about_web_view);
//        about_web_view.getSettings().setJavaScriptEnabled(true);
        about_web_view.getSettings().setDomStorageEnabled(true);
//        about_web_view.loadUrl("https://www.themoviedb.org/about/our-history");
        about_web_view.loadUrl("https://www.themoviedb.org/documentation/api");
        return v;
    }
}

