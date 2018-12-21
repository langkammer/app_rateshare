package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import br.com.rateshare.R;

public class ConfigFragment extends Fragment {

    public static final String TITULO_APPBAR = "Preferencias";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(TITULO_APPBAR);
        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.tela_config,  container, false);

        return view;
    }


}
