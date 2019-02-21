package br.com.rateshare.ui.activity;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import br.com.rateshare.R;
import br.com.rateshare.helper.LoginHelper;
import br.com.rateshare.model.User;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "FIREBASE_LOGIN";
    private LoginHelper helper;
    private FirebaseAuth firebaseAuth;
    private Intent intent;
    private DatabaseReference mDatabase;

    private CallbackManager mCallbackManager;

    public void setHelper(LoginHelper helper) {
        this.helper = helper;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.tela_login);
        final LoginHelper helper = new LoginHelper(this);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if(extras.containsKey("facebook_deslogou")) {
            helper.getBtnLoginfacebook().performClick();

        }
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

        helper.getBtnLoginFalso().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                helper.getBtnLoginfacebook().performClick();
            }
        });

        helper.getBtnLoginfacebook().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                acessarFacebookProvider(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }


            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });


       geraChave();


    }


    private void exibirProgress(boolean exibir) {
        helper.getProgressBar().setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
    public void geraChave(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "br.com.rateshare",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("ERRO KEY:", e.getMessage());


        } catch (NoSuchAlgorithmException e) {
            Log.d("ERRO KEY:", e.getMessage());

        }
    }
    private void acessarFacebookProvider(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        exibirProgress(true);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            User usuarioCriado = new User();
                            criaCollectionUsuario(usuarioCriado.criaPerfilUsuario(user,user.getDisplayName()));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void vaiParaMenu() {
        exibirProgress(false);
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



    public Boolean criaCollectionUsuario(final User user){
        boolean retorno = false;


        Query query = mDatabase.child("users").orderByChild("uid").equalTo(user.uid).limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    vaiParaMenu();
                }
                else{
                    criaPerfilNovoUsuario(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return retorno;
    }


    public void criaPerfilNovoUsuario(User user) {
        mDatabase.child("users")
                .push()
                .setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                        // Write was successful!
                        // ...
                        Log.d(TAG, "FOI CIRADO NO FIREBASE INSTANCIA DO USUARIO");
                        vaiParaMenu();
                    }
                });
    }
}
