package br.com.rateshare.helper;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;

import br.com.rateshare.R;
import br.com.rateshare.model.User;

/**
 * Created by alura on 12/08/15.
 */
public class ConfigHelper {

    private final ImageView perfilFoto;
    private final RadioButton radio_sim;
    private final RadioButton radio_nao;
    private final TextView labelNome;
    private final EditText editEmail;
    private User user;

    public ConfigHelper(View view) {
        radio_sim = view.findViewById(R.id.radio_sim);
        radio_nao = view.findViewById(R.id.radio_nao);
        labelNome = view.findViewById(R.id.text_cfg_label_nome);
        editEmail = view.findViewById(R.id.edittext_cfg_email);
        perfilFoto = view.findViewById(R.id.config_perfil_foto);
    }

    public void preencheFormulario(User userCfg) {

        user = userCfg;


    }

    public void carregaImagem(String caminhoFoto) {
        if (caminhoFoto != null) {
            Log.i("FOTO ",caminhoFoto);
            File imgFile = new File(caminhoFoto);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            getPerfilFoto().setImageBitmap(imageBitmap);
        }
    }


    public ImageView getPerfilFoto() {
        return perfilFoto;
    }

    public RadioButton getRadio_sim() {
        return radio_sim;
    }

    public RadioButton getRadio_nao() {
        return radio_nao;
    }

    public TextView getLabelNome() {
        return labelNome;
    }

    public EditText getEditEmail() {
        return editEmail;
    }
}
