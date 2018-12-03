package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.rateshare.R;
import br.com.rateshare.helper.FormPostsAdapterHelper;

public class FormNovoPostagemFragment extends Fragment {

    private View view;

    private FormPostsAdapterHelper helper;

    private String pathFoto;

    public static final String TITULO_APPBAR = "Nova Postagem";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        helper = new FormPostsAdapterHelper(this);


        Bundle parametros = getArguments();
        if (parametros != null) {
            String pathFile = (String) parametros.getSerializable("pathFile");
            putPathFoto(pathFile);
            helper.carregaImagem(this.pathFoto);

        }


        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.activity_formulario_inclui_foto,  container, false);

        return view;
    }


    public void putPathFoto(String path) {
        this.pathFoto = path;
    }
}
