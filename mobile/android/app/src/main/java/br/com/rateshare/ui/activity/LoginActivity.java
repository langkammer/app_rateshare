package br.com.rateshare.ui.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.rateshare.R;
import br.com.rateshare.helper.LoginHelper;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "FIREBASE_LOGIN";
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
                vaiParaMenu();
            }
        }

    }

    private void vaiParaMenu() {
        startActivity(new Intent(getApplicationContext(), MenuPrincipal.class));
        finish();
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

    public void logarNormal(){
        firebaseAuth.signInWithEmailAndPassword(helper.getEdiEmail().getText().toString(), helper.getEditPass().getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            vaiParaMenu();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
