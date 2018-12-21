package br.com.rateshare.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.helper.FormPostsAdapterHelper;
import br.com.rateshare.model.CategoriaModel;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.util.DataUtil;
import br.com.rateshare.util.FotoUtil;

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

        view = inflater.inflate(R.layout.tela_fragment_formulario_inclui_foto,  container, false);

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
            helper.preencheFormulario(new PostModel(getContext()));
            helper.carregaImagem(this.pathFoto);

        }

        helper.getForm_item_btn_salvar().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if(helper.validateFields().get("resposta") == "e")
                    Toast.makeText(getContext(), helper.validateFields().get("msg"), Toast.LENGTH_LONG).show();
                else
                    salvarPost();
                }
        });


        helper.getFormItemBtnCamera().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                abreCamera();
            }
        });



    }

    private void abreCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = FotoUtil.createImageFile(getContext());
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error em salvar a foto.", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "br.com.rateshare.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePictureIntent, FotoUtil.CODIGO_CAMERA);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == FotoUtil.CODIGO_CAMERA) {
                putPathFoto(FotoUtil.caminhoFoto);
            }
        }
    }

    private void salvarPost() {

        PostModel postModel = new PostModel(getContext());

        postModel.data = DataUtil.data;

        CategoriaModel categoriaSelecionada = (CategoriaModel) helper.getFormItemOptionCateg().getSelectedItem();

        postModel.id_categoria_externa = categoriaSelecionada.id_externo ;

        postModel.nota = (double) helper.getFormItemRate().getProgress();

        postModel.caminho_foto = this.pathFoto;

        postModel.titulo = helper.getFormItemEitTitulo().getText().toString();

        postModel.descricao = helper.getFormItemTexteditDescript().getText().toString();

        postModel.nome_categoria = categoriaSelecionada.nome;

        postModel.id_user_externo = "1"; // a implementar que vem do usuario logado

        postModel.num_avaliacoes = "1"; // vem do banco externamente

        postModel.flagEnvio = 0;

        PostModel post = postModel.salvar();


        Toast.makeText(getContext(), "Post Salvo com Sucesso ! " , Toast.LENGTH_LONG).show();

        return;
    }
}