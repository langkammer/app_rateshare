package br.com.rateshare.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.rateshare.R;
import br.com.rateshare.helper.NovoUsuarioHelper;
import br.com.rateshare.model.User;
import br.com.rateshare.util.DataUtil;

public class CadastroUsuarioActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Cadastrar Usuario";
    private static final String TAG = "FIREBASE_APP";
    private Intent intent;
    private NovoUsuarioHelper helper;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public void setHelper(NovoUsuarioHelper helper) {
        this.helper = helper;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tela_cadastra_usuario);
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    public void cadastrar(){
        User user = helper.getPopulateUsuario();
        mAuth.createUserWithEmailAndPassword(user.email, helper.getPass())
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    User usuarioCriado = new User();
                    criaCollectionUsuario(usuarioCriado.criaPerfilUsuario(user,helper.getEditNome().getText().toString()));
                    vaiParaMenu();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(CadastroUsuarioActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public Boolean criaCollectionUsuario(User user){
        boolean retorno = false;
        mDatabase.child("users").child(user.uid).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                        Log.d(TAG, "FOI CIRADO NO FIREBASE INSTANCIA DO USUARIO");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "NÃO FOI CIRADO NO FIREBASE INSTANCIA DO USUARIO");

                    }
                });
       return retorno;
    }


    public void voltar(){
        intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void vaiParaMenu(){
        intent = new Intent(getApplicationContext(),MenuPrincipal.class);
        startActivity(intent);
        finish();
    }



}
