package br.com.rateshare.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.model.CategoriaModel;
import br.com.rateshare.model.Postagem;
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
    public static String databaseName = new DatabaseSettings().nameDatabase;
    private SpinCategoriaAdapter adapter;
    private Spinner mySpinner;


    private Postagem postagem;

    private Context context;

    public FormPostsAdapterHelper(View view) {
        formItemEitTitulo   =   view.findViewById(R.id.form_item_edit_titulo);
        formItemOptionCateg =   view.findViewById(R.id.form_item_option_categ);
        formItemRate        =   view.findViewById(R.id.form_item_rate);
        formItemImageview   =   view.findViewById(R.id.form_item_image_view);
        formItemTexteditDescript  = view.findViewById(R.id.form_item_textedit_descript);
        form_item_btn_salvar      = view.findViewById(R.id.form_item_btn_salvar);
        this.context = view.getContext();
    }



    public Postagem pegaPostagem() {
        postagem.setTitulo(getFormItemEitTitulo().getText().toString());
        postagem.setDescricao(getFormItemTexteditDescript().getText().toString());
        //popular categoria
        List<CategoriaModel> listCat = new ArrayList<CategoriaModel>();
        postagem.setRate(Double.valueOf(getFormItemRate().getProgress()));
        postagem.setImagem((String) getFormItemImageview().getTag());
        return postagem;
    }

    public void preencheFormulario(Postagem postagem) {
        CategoriaModel categoriaModel = new CategoriaModel(context,databaseName);

        ArrayList<CategoriaModel> models =  new ArrayList<>();;

        models = new ArrayList<CategoriaModel>(categoriaModel.listar());

        ArrayAdapter<CategoriaModel> arrayAdapter  = new ArrayAdapter(context, android.R.layout.simple_spinner_item, models);

        adapter = new SpinCategoriaAdapter(context,
                android.R.layout.simple_spinner_item,
                models);
        getFormItemOptionCateg().setAdapter(adapter);



    }


    public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
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
}
