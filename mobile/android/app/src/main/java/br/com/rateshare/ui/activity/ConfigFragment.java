package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



import br.com.rateshare.R;
import br.com.rateshare.helper.ConfigHelper;
import br.com.rateshare.model.User;

public class ConfigFragment extends Fragment {

    public static final String TITULO_APPBAR = "Preferencias";

    private View view;

    private ConfigHelper helper;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;


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

        return view;
    }



    private void populaTelaConfig(User user){
        if(user!=null){
            helper.getEditEmail().setText(user.email);
            helper.getLabelNome().setText(user.nome);
            if(user.socialLogin)
                helper.carregaImagem("");
            if(user.recebeNoti)
                helper.getRadio_sim().setActivated(true);
            if(!user.recebeNoti)
                helper.getRadio_nao().setActivated(true);
        }

    }

    private void getUserByAuth(){
        String token = mAuth.getCurrentUser().getUid();
        // Database listener
        Query query;
        mDatabase.child("users").limitToFirst(1).orderByChild("uid").equalTo(token)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot user: dataSnapshot.getChildren()) {
                            Log.d("TAG", user.getKey());
                            User userBean = user.getValue(User.class);
                            populaTelaConfig(userBean);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}