package br.com.rateshare.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.model.CategoriaModel;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.ui.adapter.SpinCategoriaAdapter;

/**
 * Created by alura on 12/08/15.
 */
public class FormPostsAdapterHelper {

    private final ImageView formItemImageview;
    private final EditText formItemEitTitulo;
    private final Spinner formItemOptionCateg;
    private final RatingBar formItemRate;
    private final EditText formItemTexteditDescript;
    private final Button form_item_btn_salvar;
    private final ImageButton formItemBtnCamera;

    public static String databaseName = new DatabaseSettings().nameDatabase;
    private SpinCategoriaAdapter adapter;
    private Spinner mySpinner;


    private PostModel postagem;

    private Context context;

    public FormPostsAdapterHelper(View view) {
        formItemEitTitulo         = view.findViewById(R.id.form_item_edit_titulo);
        formItemOptionCateg       = view.findViewById(R.id.form_item_option_categ);
        formItemRate              = view.findViewById(R.id.form_item_rate);
        formItemImageview         = view.findViewById(R.id.form_item_image_view);
        formItemTexteditDescript  = view.findViewById(R.id.form_item_textedit_descript);
        form_item_btn_salvar      = view.findViewById(R.id.form_item_btn_salvar);
        formItemBtnCamera         = view.findViewById(R.id.form_item_btn_camera);
        this.context = view.getContext();
    }



    public PostModel pegaPostagem() {
        postagem.titulo = getFormItemEitTitulo().getText().toString();
        postagem.descricao = getFormItemTexteditDescript().getText().toString();
        postagem.nota = Double.valueOf(getFormItemRate().getProgress());
        postagem.id_categoria_externa = getFormItemOptionCateg().getSelectedItem().toString();
        postagem.data = (String) getFormItemImageview().getTag();
        return postagem;
    }

    public void preencheFormulario(PostModel postagem) {
        CategoriaModel categoriaModel = new CategoriaModel(context);

        ArrayList<CategoriaModel> models =  new ArrayList<>();;

        models = new ArrayList<CategoriaModel>(categoriaModel.listar());

        adapter = new SpinCategoriaAdapter(context,
                R.layout.simple_item_spiner_categoria,
                models);
        getFormItemOptionCateg().setAdapter(adapter);



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
