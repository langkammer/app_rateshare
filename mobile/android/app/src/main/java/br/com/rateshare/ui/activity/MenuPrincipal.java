package br.com.rateshare.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.Post;
import br.com.rateshare.ui.adapter.ListaPostsAdapter;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TITULO_APPBAR = "Postagens";
    private static final int POSICAO_INVALIDA = 2;
    private static final int CODIGO_REQUISICAO_ALTERA_NOTA = 1;
    private static final String CHAVE_NOTA = "nota";
    private static final int CODIGO_REQUISICAO_INSERE_NOTA = 12;
    public static final int CODIGO_CAMERA = 567;
    private String caminhoFoto;


    public ListaPostsAdapter adapter;

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

//    public void abreCamera(){
//        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File imagePath = new File(this.getFilesDir(), "images");
//        File arquivoFoto = new File(imagePath, System.currentTimeMillis() + ".jpg");
//        Uri contentUri = getUriForFile(getApplicationContext(), "br.com.rateshare.fileprovider", arquivoFoto);
//        caminhoFoto = contentUri.toString();
//        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
//        startActivityForResult(intentCamera, CODIGO_CAMERA);
//
//
//    }

    private void abreCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(), "Error while saving picture.", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "br.com.rateshare.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CODIGO_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        caminhoFoto = image.getAbsolutePath();
        return image;
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
            if (requestCode == CODIGO_CAMERA) {
                vaiParaNovaPostagem(caminhoFoto);
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
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        Intent intent;

        if(id == R.id.menu_item_ajustes){
            callFragmetSettings();
        }
        if(id == R.id.menu_item_posts){
            callFragmentPosts();
        }
        if(id == R.id.menu_item_sair){
           intent = new Intent(getApplicationContext(),LoginActivity.class);
           startActivity(intent);
           this.finish();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void vaiParaFormularioNotaActivityInsere() {
        Toast.makeText(getApplicationContext(), "Teste vaiParaFormularioNotaActivityInsere", Toast.LENGTH_SHORT).show();

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


    //para popular dados na lista da recicle view

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
