package br.com.rateshare.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.Post;
import br.com.rateshare.ui.adapter.ListaPostsAdapter;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;

public class ListaPostsActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Postagens";
    private static final int POSICAO_INVALIDA = 2;
    private static final int CODIGO_REQUISICAO_ALTERA_NOTA = 1;
    private static final String CHAVE_NOTA = "nota";
    private static final int CODIGO_REQUISICAO_INSERE_NOTA = 12;

    public ListaPostsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_posts);

        setTitle(TITULO_APPBAR);

        List<Post> todasNotas = pegaTodosPosts();
        configuraRecyclerView(todasNotas);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void vaiParaFormularioNotaActivityInsere() {
        Toast.makeText(getApplicationContext(), "Teste vaiParaFormularioNotaActivityInsere", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //nao implementado
    }

    private void altera(Post post, int posicao) {
        Toast.makeText(getApplicationContext(), "altera", Toast.LENGTH_SHORT).show();

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

    private void adiciona(Post post) {
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
        RecyclerView listaPosts = findViewById(R.id.lista_posts_recyclerview);
        configuraAdapter(todosPosts, listaPosts);
        configuraItemTouchHelper(listaPosts);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {

        // configura deslize
//        ItemTouchHelper itemTouchHelper =
//                new ItemTouchHelper(new PostItemTouchHelperCallback(adapter));
//        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Post> todosPosts, RecyclerView listaPosts) {
        adapter = new ListaPostsAdapter(this, todosPosts);
        listaPosts.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Post post, int posicao) {
                vaiParaFormularioNotaActivityAltera(post, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Post post, int posicao) {
        Toast.makeText(getApplicationContext(), "Teste botao", Toast.LENGTH_SHORT).show();
    }



    public List<Post> pegaTodosPosts(){

        List<Post> listPosts = new ArrayList<Post>();

        Post post;

        for(int i = 1; i <= 3; i++){

            post = new Post();

            switch (i){
                case 1:
                    post.setCategoria("Lugares");
                    post.setDescricao("Teste descricao 1");
                    post.setNumAvaliacoes("200 Avaliações");
                    post.setImagem("@drawable/belo_horizonte_mg");
                    post.setRate("4");
                    post.setTitulo("Belo Horizonte");
                    break;
                case 2:
                    post.setCategoria("Bebidas");
                    post.setDescricao("Vinho Sangue de Boi");
                    post.setNumAvaliacoes("12 Avaliações");
                    post.setImagem("@drawable/saungeboi");
                    post.setRate("5");
                    post.setTitulo("Vinho Sangue de Boi");
                    break;
                case 3:
                    post.setCategoria("Comidas");
                    post.setDescricao("Picanha Top");
                    post.setNumAvaliacoes("750 Avaliações");
                    post.setImagem("@drawable/picanha");
                    post.setRate("3");
                    post.setTitulo("Picanha TOP +++");
                    break;
            }



            listPosts.add(post);

        }

        return listPosts;
    }
}
