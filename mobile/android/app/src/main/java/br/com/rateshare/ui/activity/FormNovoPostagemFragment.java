package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.helper.FormPostsAdapterHelper;
import br.com.rateshare.model.Post;
import br.com.rateshare.model.Postagem;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;

public class FormNovoPostagemFragment extends Fragment {

    private View view;

    private FormPostsAdapterHelper helper;

    private String pathFoto;

    public static final String TITULO_APPBAR = "Nova Postagem";

    public static String databaseName = new DatabaseSettings().nameDatabase;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.activity_formulario_inclui_foto,  container, false);

        return view;
    }




    public void putPathFoto(String path) {
        this.pathFoto = path;
    }

    public void setHelper(FormPostsAdapterHelper helper) {
        this.helper = helper;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper = new FormPostsAdapterHelper(view);
        setHelper(helper);
        Bundle parametros = getArguments();
        if (parametros != null) {
            String pathFile = (String) parametros.getSerializable("pathFile");
            putPathFoto(pathFile);
            helper.preencheFormulario(new Postagem());
            helper.carregaImagem(this.pathFoto);

        }

        helper.getForm_item_btn_salvar().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if(helper.validateFields())
                    Toast.makeText(getContext(), "Salvar", Toast.LENGTH_LONG).show();

            }
        });

    }


}
