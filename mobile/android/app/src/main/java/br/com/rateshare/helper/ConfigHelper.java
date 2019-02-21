package br.com.rateshare.helper;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
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
    private final Button btnSalvar;
    private User user;

    public ConfigHelper(View view) {
        radio_sim = view.findViewById(R.id.radio_sim);
        radio_nao = view.findViewById(R.id.radio_nao);
        labelNome = view.findViewById(R.id.text_cfg_label_nome);
        editEmail = view.findViewById(R.id.edittext_cfg_email);
        perfilFoto = view.findViewById(R.id.config_perfil_foto);
        btnSalvar = view.findViewById(R.id.form_salvar_config);
    }

    public void preencheFormulario(User userCfg) {
        user = userCfg;
    }

    public void carregaImagem(FirebaseUser user) {
        if (user != null) {
            Picasso.get().load(user.getPhotoUrl().toString()).into(getPerfilFoto());

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

    public Button getBtnSalvar(){ return btnSalvar; }

    public EditText getEditEmail() {
        return editEmail;
    }
}
