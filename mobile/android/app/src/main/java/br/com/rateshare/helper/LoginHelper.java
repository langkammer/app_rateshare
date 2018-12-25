package br.com.rateshare.helper;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.rateshare.R;

/**
 * Created by alura on 12/08/15.
 */
public class LoginHelper {

    private final Button btnLoginNormal;
    private final Button btnLoginfacebook;
    private final Button btnLoginTwitter;
    private final Button btnEsqueciSenha;
    private final Button btnCadastrar;
    private final EditText ediEmail;
    private final EditText editPass;


    public LoginHelper(Activity act) {
        btnLoginNormal         = act.findViewById(R.id.btnLogin);
        btnLoginfacebook       = act.findViewById(R.id.btnFacebookLogin);
        btnLoginTwitter        = act.findViewById(R.id.btnTwtLogin);
        btnEsqueciSenha        = act.findViewById(R.id.esquciSenha);
        btnCadastrar           = act.findViewById(R.id.btnCadastrar);
        ediEmail               = act.findViewById(R.id.email);
        editPass               = act.findViewById(R.id.senha);
    }


    public Button getBtnLoginNormal() {
        return btnLoginNormal;
    }

    public Button getBtnLoginfacebook() {
        return btnLoginfacebook;
    }

    public Button getBtnLoginTwitter() {
        return btnLoginTwitter;
    }

    public Button getBtnEsqueciSenha() {
        return btnEsqueciSenha;
    }

    public Button getBtnCadastrar() {
        return btnCadastrar;
    }

    public EditText getEdiEmail(){
        return ediEmail;
    }

    public EditText getEditPass(){
        return editPass;
    }
}
