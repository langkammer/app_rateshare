package br.com.rateshare.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.Categoria;
import br.com.rateshare.model.Post;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.ui.adapter.ListaPostsAdapter;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;
import br.com.rateshare.util.DataUtil;
import br.com.rateshare.util.FotoUtil;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TITULO_APPBAR = "Postagens";
    private static final int POSICAO_INVALIDA = 2;
    private static final int CODIGO_REQUISICAO_ALTERA_NOTA = 1;
    private static final String CHAVE_NOTA = "nota";
    private static final int CODIGO_REQUISICAO_INSERE_NOTA = 12;
    private static final String TAG = "CAT";


    public ListaPostsAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private  Toolbar toolbar;

    private Intent intent;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        montaTelaInicial();
        callFragmentPosts();
        setTitle(TITULO_APPBAR);

    }



    @SuppressLint("ResourceType")
    public void callFragmentPosts(){

        FragmentTransaction tx = getFragmentTransaction();

        tx.replace(R.id.frame_principal, new ListaPostsFrament());

        tx.commit();

    }

    @SuppressLint("ResourceType")
    public void callFragmentsMeusPosts(){

        FragmentTransaction tx = getFragmentTransaction();

        tx.replace(R.id.frame_principal, new ListaMeusPostsFrament());

        tx.commit();

    }
    private void abreCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = FotoUtil.createImageFile(getApplicationContext());
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "Error em salvar a foto.", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "br.com.rateshare.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, FotoUtil.CODIGO_CAMERA);
            }
        }
    }


    private void vaiParaNovaPostagem(String caminhoFoto) {
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction tx = manager.beginTransaction();

        FormNovoPostagemFragment detalhesFragment = new FormNovoPostagemFragment();

        Bundle parametros = new Bundle();

        parametros.putSerializable("pathFile", caminhoFoto);

        detalhesFragment.setArguments(parametros);

        tx.replace(R.id.frame_principal, detalhesFragment);

        tx.addToBackStack(null);

        tx.commitAllowingStateLoss();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FotoUtil.CODIGO_CAMERA) {
                vaiParaNovaPostagem(FotoUtil.caminhoFoto);
            }
        }

    }

    @SuppressLint("ResourceType")
    public void callFragmetSettings(){

        FragmentTransaction tx = getFragmentTransaction();

        tx.replace(R.id.frame_principal, new ConfigFragment());

        tx.commit();

    }


    private void montaTelaInicial() {
        setContentView(R.layout.tela_menu_principal);
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }




    private FragmentTransaction getFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.beginTransaction();
    }



    @Override
    public void onBackPressed() {
        //registra botão de volta
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(getApplicationContext(), "teste Voltar", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // seleciona função de foto
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.abre_foto) {
            Toast.makeText(getApplicationContext(), "Entrou na opção fotografia", Toast.LENGTH_SHORT).show();
            abreCamera();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.menu_item_ajustes){
            callFragmetSettings();
        }
        if(id == R.id.menu_item_posts){
            callFragmentPosts();
        }
        if(id == R.id.menu_meus_posts){
            callFragmentsMeusPosts();
        }
        if(id == R.id.menu_item_sair){
            if(mAuth.getCurrentUser().getProviders().get(0).equals("facebook.com")){
                Bundle parametros = new Bundle();

                parametros.putSerializable("facebook_deslogou", true);

                mAuth.signOut();

                this.intent = new Intent(getApplicationContext(),LoginActivity.class).putExtra("facebook_deslogou",true);

                startActivity(this.intent);
            }
            else{
                mAuth.signOut();

                this.intent = new Intent(getApplicationContext(),LoginActivity.class);

                startActivity(this.intent);
            }


            this.finish();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void vaiParaFormularioNotaActivityInsere() {
        Toast.makeText(getApplicationContext(), "Teste vaiParaFormularioNotaActivityInsere", Toast.LENGTH_SHORT).show();

    }

    private void altera(PostModel post, int posicao) {
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


    //para popular dados na lista da recicle view
    public List<PostModel> pegaTodosPostsLocal(){

        List<PostModel> listPosts = new ArrayList<PostModel>();

        listPosts = new PostModel(getApplicationContext()).listar();

        return listPosts;
    }
}