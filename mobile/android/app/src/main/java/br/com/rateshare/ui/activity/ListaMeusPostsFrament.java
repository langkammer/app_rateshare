package br.com.rateshare.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.ui.adapter.ListaMeusPostsAdapter;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;

public class ListaMeusPostsFrament extends Fragment {

    public static final String TITULO_APPBAR = "Minhas Postagens";
    private static final int POSICAO_INVALIDA = 2;
    private static final int CODIGO_REQUISICAO_ALTERA_NOTA = 1;
    private static final String CHAVE_NOTA = "nota";
    private static final int CODIGO_REQUISICAO_INSERE_NOTA = 12;

    public ListaMeusPostsAdapter adapter;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(TITULO_APPBAR);
        return getView(inflater, container);

    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.tela_meus_posts, container, false);


        List<PostModel> todasNotas = pegaTodosPosts();
        configuraRecyclerView(todasNotas);

        return view;
    }

    private void configuraRecyclerView(List<PostModel> todosPosts) {
        RecyclerView listaPosts = view.findViewById(R.id.lista_dos_meus_posts_RecyclerView);
        configuraAdapter(todosPosts, listaPosts);
        configuraItemTouchHelper(listaPosts);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {

        // configura deslize
//        ItemTouchHelper itemTouchHelper =
//                new ItemTouchHelper(new PostItemTouchHelperCallback(adapter));
//        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<PostModel> todosPosts, RecyclerView listaPosts) {
        adapter = new ListaMeusPostsAdapter(getContext(), todosPosts);
        listaPosts.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(PostModel post, int posicao) {
                vaiParaFormularioNotaActivityAltera(post, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(PostModel post, int posicao) {
        Toast.makeText(getContext(), "Teste botao", Toast.LENGTH_SHORT).show();
    }



    public List<PostModel> pegaTodosPosts(){

        List<PostModel> listPosts = new ArrayList<PostModel>();

        listPosts = new PostModel(getContext()).listar();

        return listPosts;
    }
}
