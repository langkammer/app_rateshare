package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.rateshare.R;
import br.com.rateshare.helper.ConfigHelper;
import br.com.rateshare.model.User;

public class ConfigFragment extends Fragment {

    public static final String TITULO_APPBAR = "Preferencias";

    private View view;

    private ConfigHelper helper;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private User user;

    private String key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(TITULO_APPBAR);
        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.tela_config, container, false);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


        helper = new ConfigHelper(view);


        helper.getBtnSalvar().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                salvarConfig();
            }
        });

        getUserByAuth();
        return view;
    }



    private void salvarConfig(){
        if(helper.getRadio_sim().isChecked())
            this.user.recebeNoti = true;
        if(helper.getRadio_nao().isChecked())
            this.user.recebeNoti = false;
        if(!helper.getEditEmail().getText().toString().equals(null))
            this.user.email = helper.getEditEmail().getText().toString();


        if(this.user.uid!=null){
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/users/" + this.key, user);
            mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(), "Configuração salva com sucesso ! " , Toast.LENGTH_LONG).show();
                }
            });

        }
        else{
            mDatabase.child("users")
                    .setValue(user, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            Toast.makeText(getContext(), "Configuração salva com sucesso ! " , Toast.LENGTH_LONG).show();

                        }
                    });
        }

    }

    private void populaTelaConfig(User user,String key){
        if(user!=null){
            helper.getEditEmail().setText(user.email);
            helper.getLabelNome().setText(user.nome);
            if(user.recebeNoti)
                helper.getRadio_sim().setChecked(true);
            if(!user.recebeNoti)
                helper.getRadio_nao().setChecked(true);
            if(user.tipoSocialLogin.equals("facebook.com"))
                helper.carregaImagem(mAuth.getCurrentUser());
            if(key!=null)
                this.key = key;
            this.user = user;
        }

    }

    private void getUserByAuth(){
        String token = mAuth.getCurrentUser().getUid();
//        query.addListenerForSingleValueEvent(listnerConfig());

        Query  query = mDatabase.child("users").orderByChild("uid").equalTo(token).limitToFirst(1);

        query.addListenerForSingleValueEvent(listnerConfig());

    }

    public ValueEventListener listnerConfig(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map of all products which has city equal to mCurrentUserCity
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    populaTelaConfig(user,postSnapshot.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        return valueEventListener;
    }
}