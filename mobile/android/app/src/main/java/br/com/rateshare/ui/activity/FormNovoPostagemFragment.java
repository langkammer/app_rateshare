package br.com.rateshare.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.helper.FormPostsAdapterHelper;
import br.com.rateshare.model.Categoria;
import br.com.rateshare.model.CategoriaModel;
import br.com.rateshare.model.Post;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.util.DataUtil;
import br.com.rateshare.util.FotoUtil;

public class FormNovoPostagemFragment extends Fragment {

    private static final String TAG = "FIREBASE NOVO POST";
    private View view;

    private FormPostsAdapterHelper helper;

    private String pathFoto;

    public static final String TITULO_APPBAR = "Nova Postagem";

    public static String databaseName = new DatabaseSettings().nameDatabase;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;



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
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        Post postBean = new Post();

        postBean.data = DataUtil.data;

        Categoria categoriaSelecionada = (Categoria) helper.getFormItemOptionCateg().getSelectedItem();

        postBean.categoria = categoriaSelecionada;

        HashMap<String, Integer> stars = new HashMap<>();

        stars.put(mAuth.getUid(),helper.getFormItemRate().getProgress());

        postBean.stars =  stars;

        postBean.pathFoto = this.pathFoto;

        postBean.titulo = helper.getFormItemEitTitulo().getText().toString();

        postBean.descricao = helper.getFormItemTexteditDescript().getText().toString();

        postBean.numAvaliacoes = 1;

        postBean.aprovado = false;
        criaPostagemFirebase(postBean);

    }

    public void criaPostagemFirebase(final Post post){
        mDatabase.child("posts").child(mAuth.getUid()).setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "FOI CIRADO NO FIREBASE INSTANCIA DO USUARIO");
                        vincululaPostMeuPost(post);
                        getActivity().getFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "NÃO FOI CIRADO NO FIREBASE INSTANCIA DO USUARIO");

                    }
                });
        Toast.makeText(getContext(), "Post Salvo com Sucesso ! " , Toast.LENGTH_LONG).show();

        return;
    }

    public void vincululaPostMeuPost(Post post){
        mDatabase.child("meus-posts").child(mAuth.getUid()).setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "FOI CIRADO NO FIREBASE INSTANCIA DO USUARIO");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "NÃO FOI CIRADO NO FIREBASE INSTANCIA DO USUARIO");

                    }
                });
        Toast.makeText(getContext(), "Post Salvo com Sucesso ! " , Toast.LENGTH_LONG).show();

        return;
    }
}