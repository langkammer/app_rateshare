package br.com.rateshare.ui.activity;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.rateshare.R;
import br.com.rateshare.helper.LoginHelper;


public class LoginActivity extends AppCompatActivity {

    private LoginHelper helper;
    private FirebaseAuth firebaseAuth;
    private Intent intent;

    public void setHelper(LoginHelper helper) {
        this.helper = helper;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.tela_login);
        LoginHelper helper = new LoginHelper(this);
        setHelper(helper);


        helper.getBtnCadastrar().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                cadastrar();
            }
        });
        helper.getBtnLoginNormal().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                logarNormal();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            if(currentUser.getEmail() != null) {
                startActivity(new Intent(getApplicationContext(), MenuPrincipal.class));
                finish();
            }
        }

    }



    public void cadastrar(){
        intent = new Intent(getApplicationContext(),CadastroUsuarioActivity.class);
        startActivity(intent);
        finish();
    }



    private FragmentTransaction getFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.beginTransaction();
    }

    public static void logarNormal(){

    }
}
