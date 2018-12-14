package br.com.rateshare.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import java.io.File;

import br.com.rateshare.R;
import br.com.rateshare.model.Postagem;

/**
 * Created by alura on 12/08/15.
 */
public class FormPostsAdapterHelper {

    private final ImageView formItemImageview;
    private final EditText formItemEitTitulo;
    private final Spinner formItemOptionCateg;
    private final RatingBar formItemRate;
    private final EditText formItemTexteditDescript;

    private Postagem postagem;

    private Context context;

    public FormPostsAdapterHelper(View view) {
        formItemEitTitulo   =   view.findViewById(R.id.form_item_edit_titulo);
        formItemOptionCateg =   view.findViewById(R.id.form_item_option_categ);
        formItemRate        =   view.findViewById(R.id.form_item_rate);
        formItemImageview   =   view.findViewById(R.id.form_item_image_view);
        formItemTexteditDescript  = view.findViewById(R.id.form_item_textedit_descript);
        this.context = view.getContext();
        postagem = new Postagem();
    }

    public Postagem pegaPostagem() {
        postagem.setTitulo(formItemEitTitulo.getText().toString());
        postagem.setDescricao(formItemTexteditDescript.getText().toString());
//        Categoria categoria = new Categoria();
//        if(formItemOptionCateg.getSelectedItem().toString()!=null)
//            postagem.setCategoria(categoriaDAO.getCategoriaByIdExterno(formItemOptionCateg.getSelectedItem().toString()));
        postagem.setRate(Double.valueOf(formItemRate.getProgress()));
        postagem.setImagem((String) formItemImageview.getTag());
        return postagem;
    }

    public void preencheFormulario(Postagem postagem) {
        formItemEitTitulo.setText(postagem.getTitulo());
        //formItemOptionCateg.fin(aluno.getEndereco()); a implementar
        formItemTexteditDescript.setText(postagem.getDescricao());
        formItemRate.setProgress(postagem.getRate().intValue());
        carregaImagem(postagem.getImagem());
        this.postagem = postagem;
    }


    public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            File imgFile = new File(caminhoFoto);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            formItemImageview.setImageBitmap(imageBitmap);
        }
    }
}
