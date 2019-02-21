package br.com.rateshare.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.rateshare.R;
import br.com.rateshare.model.Categoria;
import br.com.rateshare.model.Post;
import br.com.rateshare.ui.adapter.SpinCategoriaAdapter;

import static android.view.View.VISIBLE;

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
    private final ProgressBar progressBar2;

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
        progressBar2              = view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.context = view.getContext();
    }

    public Post pegaPostagem() {
        postagem.titulo = getFormItemEitTitulo().getText().toString();
        postagem.descricao = getFormItemTexteditDescript().getText().toString();
        postagem.data = (String) getFormItemImageview().getTag();
        return postagem;
    }

    public void preencheFormulario(Post post,boolean ehAutor) {
        postagem = post;
        if(post.getKey()!=null){
            try{
                setPost(post,ehAutor);
            }
            catch (Exception e){
                Log.i("GET IMG",e.getMessage());
            }
        }
        getCategorias(null);
    }

    public void setPost(Post post,boolean ehAutor) throws IOException {
        exibirProgress(true);
        getFormItemEitTitulo().setText(post.titulo);
        getFormItemTexteditDescript().setText(post.descricao);
        getFormItemRate().setRating(getNota(post.stars));
        getCategorias(post.categoria);
        if(!ehAutor){
            getForm_item_btn_salvar().setVisibility(View.INVISIBLE);
            getFormItemOptionCateg().setClickable(false);
            getFormItemBtnCamera().setVisibility(View.INVISIBLE);
        }

        final File localFile = File.createTempFile("image","jpg");

        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("posts/"+post.getKey());
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                formItemImageview.setImageBitmap(bitmap);
                exibirProgress(false);

            }
        });

    }

    private void exibirProgress(boolean exibir) {
        progressBar.setVisibility(exibir ? VISIBLE : View.GONE);
    }

    public float getNota(Map<String, Integer> map){
        int total = 0;
        int soma = 0;
        Set<String> chaves = map.keySet();
        for (String chave : chaves)
        {
            if(chave != null){
                System.out.println(chave + map.get(chave));
                total ++;
                soma += (Integer) map.get(chave);
            }
        }

        return soma / total;
    }

    public String getAvaliacoes(Map<String, Integer> map){
        Integer total = 0;
        Set<String> chaves = map.keySet();
        for (String chave : chaves)
        {
            if(chave != null){
                total ++;
            }
        }

        return Integer.toString(total);
    }

    public void getCategorias(@Nullable final Categoria cat){

        // Database listener
        mDatabase.child("categorias").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Categoria> listaCategorias = new ArrayList<Categoria>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Categoria a = postSnapshot.getValue(Categoria.class);
                    a.setKey(postSnapshot.getKey());
                    listaCategorias.add(a);
                }

                setSpiner(listaCategorias,cat);

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    private void setSpiner(List<Categoria> listaCategorias,Categoria cat) {
        spinnerAdapter = new SpinCategoriaAdapter(context,    R.layout.simple_item_spiner_categoria,        listaCategorias );
        spinnerAdapter.setDropDownViewResource( R.layout.simple_item_spiner_categoria);
        getFormItemOptionCateg().setAdapter(spinnerAdapter);
        if(cat != null)
            getFormItemOptionCateg().setSelection(spinnerAdapter.getPosicao(cat),true);
        else
            getFormItemOptionCateg().setSelection(0,true);
    }

    public SpinCategoriaAdapter getSpinnerAdapter(){
        return spinnerAdapter;
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

    public ProgressBar getProgressBar2(){ return progressBar2;}


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
