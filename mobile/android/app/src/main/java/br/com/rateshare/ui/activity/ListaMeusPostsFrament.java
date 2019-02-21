package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.Post;
import br.com.rateshare.ui.adapter.ListaMeusPostsAdapter;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;

public class ListaMeusPostsFrament extends Fragment {

    public static final String TITULO_APPBAR = "Minhas Postagens";
    private static final int POSICAO_INVALIDA = 2;
    private static final int CODIGO_REQUISICAO_ALTERA_NOTA = 1;
    private static final String CHAVE_NOTA = "nota";
    private static final int CODIGO_REQUISICAO_INSERE_NOTA = 12;

    private List<Post> listMyPosts;

    public ListaMeusPostsAdapter adapter;

    private View view;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(TITULO_APPBAR);
        return getView(inflater, container);

    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.tela_meus_posts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.lista_posts_recyclerview);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        pegaMeusPosts();


        return view;
    }


    private void configuraRecyclerView(List<Post> todosPosts) {
        RecyclerView listaPosts = view.findViewById(R.id.lista_dos_meus_posts_RecyclerView);
        configuraAdapter(todosPosts, listaPosts);
        configuraItemTouchHelper(listaPosts);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {

    }

    private void configuraAdapter(List<Post> todosPosts, RecyclerView listaPosts) {
        adapter = new ListaMeusPostsAdapter(getContext(), todosPosts);
        listaPosts.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Post post, int posicao) {
                vaiParaFormularioNotaActivityAltera(post, posicao);
            }
        });
    }


    private void vaiParaFormularioNotaActivityAltera(Post post, int posicao) {
        goToPost(post);
    }

    private void preencheMeusPosts(DataSnapshot dataSnapshot){
        listMyPosts.clear();
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Post a = singleSnapshot.getValue(Post.class);
            a.setKey(singleSnapshot.getKey());
            listMyPosts.add(a);
        }
        configuraRecyclerView(listMyPosts);

    }

    private void goToPost(Post post) {
        FormVerPostagemFragment verPost = new FormVerPostagemFragment();
        Bundle args = new Bundle();
        args.putString("keyPost", post.getKey());
        args.putBoolean("autor", true);
        verPost.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_meus_posts, verPost);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void pegaMeusPosts(){
        System.out.println("ACIONOU MEUS POSTS");
        listMyPosts  = new ArrayList<Post>();
        // Database listener
        Query query;
        mDatabase.child("posts").orderByChild("userKey").equalTo(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        preencheMeusPosts(dataSnapshot);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
