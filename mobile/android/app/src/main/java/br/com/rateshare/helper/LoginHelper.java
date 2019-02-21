package br.com.rateshare.helper;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.login.widget.LoginButton;

import br.com.rateshare.R;

import static android.view.View.VISIBLE;

/**
 * Created by alura on 12/08/15.
 */
public class LoginHelper {

    private final Button btnLoginNormal;
    private final LoginButton btnLoginfaceboo;
    private final Button btnLoginTwitter;
    private final Button btnFalsoFacebook;

    private final Button btnEsqueciSenha;
    private final Button btnCadastrar;
    private final EditText ediEmail;
    private final EditText editPass;
    private final ProgressBar progressBar;


    public LoginHelper(Activity act) {
        btnLoginNormal         = act.findViewById(R.id.btnLogin);
        btnLoginfaceboo       = act.findViewById(R.id.btnFacebookLogin);
        btnLoginTwitter        = act.findViewById(R.id.btnTwtLogin);
        btnEsqueciSenha        = act.findViewById(R.id.esquciSenha);
        btnCadastrar           = act.findViewById(R.id.btnCadastrar);
        ediEmail               = act.findViewById(R.id.email);
        editPass               = act.findViewById(R.id.senha);
        btnFalsoFacebook       = act.findViewById(R.id.btnFalsoFacebook);
        progressBar             = act.findViewById(R.id.progressBarLogin);
    }

    private void exibirProgress(boolean exibir) {
        progressBar.setVisibility(exibir ? VISIBLE : View.GONE);
    }

    public ProgressBar getProgressBar(){ return progressBar;}

    public Button getBtnLoginNormal() {
        return btnLoginNormal;
    }

    public LoginButton getBtnLoginfacebook() {
        btnLoginfaceboo.setReadPermissions("email", "public_profile");

        return btnLoginfaceboo;
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

    public Button getBtnLoginFalso() {
        return btnFalsoFacebook;
    }

    public EditText getEdiEmail(){
        return ediEmail;
    }

    public EditText getEditPass(){
        return editPass;
    }
}
