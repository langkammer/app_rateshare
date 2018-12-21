package br.com.rateshare.helper;


import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.rateshare.R;

/**
 * Created by alura on 12/08/15.
 */
public class NovoUsuarioHelper {

    private final EditText editNome;
    private final EditText editEmail;
    private final EditText passSenha;
    private final Button btnCadastrar;
    private final Button btnVoltar;


    public NovoUsuarioHelper(Activity act) {
        editNome         = act.findViewById(R.id.nome);
        editEmail        = act.findViewById(R.id.email);
        passSenha        = act.findViewById(R.id.senha);
        btnCadastrar     = act.findViewById(R.id.btnCadastrar);
        btnVoltar        = act.findViewById(R.id.btnVoltar);
    }


    public EditText getEditNome() {
        return editNome;
    }

    public EditText getEditEmail() {
        return editEmail;
    }

    public EditText getPassSenha() {
        return passSenha;
    }

    public Button getBtnCadastrar() {
        return btnCadastrar;
    }

    public Button getBtnVoltar() {
        return btnVoltar;
    }
}
