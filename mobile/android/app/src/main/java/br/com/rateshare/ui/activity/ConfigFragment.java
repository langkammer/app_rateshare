package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.com.rateshare.R;

public class ConfigFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.activity_config,  container, false);

        return view;
    }
}
