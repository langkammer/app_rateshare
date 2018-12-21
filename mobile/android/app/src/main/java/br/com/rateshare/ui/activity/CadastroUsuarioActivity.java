package br.com.rateshare.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.rateshare.R;
import br.com.rateshare.helper.NovoUsuarioHelper;

public class CadastroUsuarioActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Cadastrar Usuario";
    private Intent intent;
    private NovoUsuarioHelper helper;
    private FirebaseAuth firebaseAuth;

    public void setHelper(NovoUsuarioHelper helper) {
        this.helper = helper;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.tela_login);
        NovoUsuarioHelper helper = new NovoUsuarioHelper(this);
        setHelper(helper);


        helper.getBtnCadastrar().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                cadastrar();
            }
        });

        helper.getBtnVoltar().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                voltar();
            }
        });


    }



    public void cadastrar(){

    }

    public void voltar(){
        intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }



}
