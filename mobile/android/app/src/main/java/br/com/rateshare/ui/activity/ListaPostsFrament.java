package br.com.rateshare.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.helper.FormPostsAdapterHelper;
import br.com.rateshare.model.Categoria;
import br.com.rateshare.model.Post;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.ui.adapter.ListaPostsAdapter;
import br.com.rateshare.ui.adapter.SpinCategoriaAdapter;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;
import br.com.rateshare.ui.helper.callback.PostItemTouchHelperCallback;

public class ListaPostsFrament extends Fragment {

    public static final String TITULO_APPBAR = "Postagens";
    private static final int POSICAO_INVALIDA = 2;
    private static final int CODIGO_REQUISICAO_ALTERA_NOTA = 1;
    private static final String CHAVE_NOTA = "nota";
    private static final int CODIGO_REQUISICAO_INSERE_NOTA = 12;
    private DatabaseReference mDatabase;
    private List<Post> listPosts;
    public ListaPostsAdapter adapter;

    private FormPostsAdapterHelper helper;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        view = inflater.inflate(R.layout.tela_lista_posts, container, false);

        pegaTodosPosts();


        return view;
    }


    private void vaiParaFormularioNotaActivityInsere() {
        Toast.makeText(getContext(), "Teste vaiParaFormularioNotaActivityInsere", Toast.LENGTH_SHORT).show();
    }


    private void altera(PostModel post, int posicao) {
        Toast.makeText(getContext(), "altera", Toast.LENGTH_SHORT).show();

    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private void adiciona(PostModel post) {
        //a implementar
    }

    private boolean ehResultadoInsereNota(int requestCode, Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Post> todosPosts) {
        RecyclerView listaPosts = view.findViewById(R.id.lista_posts_recyclerview);
        configuraAdapter(todosPosts, listaPosts);
        configuraItemTouchHelper(listaPosts);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {

        // configura deslize
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new PostItemTouchHelperCallback(adapter));
//        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Post> todosPosts, RecyclerView listaPosts) {
        adapter = new ListaPostsAdapter(getContext(), todosPosts);
        listaPosts.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Post post, int posicao) {
                vaiParaFormularioNotaActivityAltera(post, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Post post, int posicao) {
        Toast.makeText(getContext(), "Teste botao", Toast.LENGTH_SHORT).show();
    }



    public void pegaTodosPosts(){

        listPosts  = new ArrayList<Post>();
        // Database listener
        Query query;
        mDatabase.child("posts").orderByChild("aprovado").equalTo(true)
        .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listPosts.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post a = postSnapshot.getValue(Post.class);
                    a.setKey(postSnapshot.getKey());
                    listPosts.add(a);
                }
                configuraRecyclerView(listPosts);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }




        });

    }
}
