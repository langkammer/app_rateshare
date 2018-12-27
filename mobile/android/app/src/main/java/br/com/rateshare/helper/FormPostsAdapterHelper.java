package br.com.rateshare.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.model.Categoria;
import br.com.rateshare.model.CategoriaModel;
import br.com.rateshare.model.Post;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.model.User;
import br.com.rateshare.ui.adapter.SpinCategoriaAdapter;

/**
 * Created by alura on 12/08/15.
 */
public class FormPostsAdapterHelper {

    private static final String TAG = "firebase ";
    private final ImageView formItemImageview;
    private final EditText formItemEitTitulo;
    private final Spinner formItemOptionCateg;
    private final RatingBar formItemRate;
    private final EditText formItemTexteditDescript;
    private final Button form_item_btn_salvar;
    private final ImageButton formItemBtnCamera;
    private final ProgressBar progressBar;
    private SpinCategoriaAdapter spinnerAdapter;
    private DatabaseReference mDatabase;

    private ArrayAdapter<Categoria> categoriaAdapter;


    private Post postagem;

    private Context context;

    private String keyCategoria;


    public FormPostsAdapterHelper(View view) {
        formItemEitTitulo         = view.findViewById(R.id.form_item_edit_titulo);
        formItemOptionCateg       = view.findViewById(R.id.form_item_option_categ);
        formItemRate              = view.findViewById(R.id.form_item_rate);
        formItemImageview         = view.findViewById(R.id.form_item_image_view);
        formItemTexteditDescript  = view.findViewById(R.id.form_item_textedit_descript);
        form_item_btn_salvar      = view.findViewById(R.id.form_item_btn_salvar);
        formItemBtnCamera         = view.findViewById(R.id.form_item_btn_camera);
        progressBar               = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.context = view.getContext();
    }



    public Post pegaPostagem() {
        postagem.titulo = getFormItemEitTitulo().getText().toString();
        postagem.descricao = getFormItemTexteditDescript().getText().toString();
//        postagem.stars = Double.valueOf(getFormItemRate().getProgress());
//        postagem.id_categoria_externa = getFormItemOptionCateg().getSelectedItem().toString();
        postagem.data = (String) getFormItemImageview().getTag();
        return postagem;
    }

    public void preencheFormulario(Post post) {

        postagem = post;
        getCategorias();


    }

    public void getCategorias(){

        // Database listener
        mDatabase.child("categorias").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Categoria> listaCategorias = new ArrayList<Categoria>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Categoria a = postSnapshot.getValue(Categoria.class);
                    listaCategorias.add(a);
                }

//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,  R.layout.simple_item_spiner_categoria, listaCategorias);
//                arrayAdapter.setDropDownViewResource(R.layout.simple_item_spiner_categoria);
//                spinner.setAdapter(skularAdapter);

                spinnerAdapter = new SpinCategoriaAdapter(context,    R.layout.simple_item_spiner_categoria,        listaCategorias );
                spinnerAdapter.setDropDownViewResource( R.layout.simple_item_spiner_categoria);
                formItemOptionCateg.setAdapter(spinnerAdapter);
                formItemOptionCateg.setSelection(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


    public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Log.i("FOTO ",caminhoFoto);
            File imgFile = new File(caminhoFoto);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            getFormItemImageview().setImageBitmap(imageBitmap);
        }
    }


    public ImageView getFormItemImageview() {
        return formItemImageview;
    }

    public EditText getFormItemEitTitulo() {
        return formItemEitTitulo;
    }

    public Spinner getFormItemOptionCateg() {
        return formItemOptionCateg;
    }

    public RatingBar getFormItemRate() {
        return formItemRate;
    }

    public EditText getFormItemTexteditDescript() {
        return formItemTexteditDescript;
    }

    public Button getForm_item_btn_salvar() {
        return form_item_btn_salvar;
    }

    public ProgressBar getProgressBar(){ return progressBar;}

    public HashMap<String,String> validateFields(){
        HashMap<String,String>  setResult = new HashMap<String,String> ();
        setResult.put("resposta","s");
        setResult.put("msg","Sucesso !");

        //valida se tem preenchimento de stars
        if(getFormItemRate().getProgress() == 0) {
            setResult.put("resposta","e");
            setResult.put("msg","Informe a quantidade de estrela que deseja avaliar a foto");
            return setResult;
        }
        if(getFormItemEitTitulo().getText().toString().isEmpty()){
            setResult.put("resposta","e");
            setResult.put("msg","Informe um titulo");
            return setResult;
        }
        if(getFormItemTexteditDescript().getText().toString().isEmpty()){
            setResult.put("resposta","e");
            setResult.put("msg","Informe um descrição");
            return setResult;
        }
        if(getFormItemOptionCateg().getSelectedItem() == null){
            setResult.put("resposta","e");
            setResult.put("msg","Informe uma categoria");
            return setResult;
        }
        return setResult;
    }

    public ImageButton getFormItemBtnCamera() {
        return formItemBtnCamera;
    }


}
