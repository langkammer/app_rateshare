package br.com.rateshare.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.Post;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.ui.adapter.ListaPostsAdapter;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;
import br.com.rateshare.ui.adapter.listener.OnRateChangeListener;

public class ListaPostsFrament extends Fragment {

    public static final String TITULO_APPBAR = "Postagens";
    private static final int POSICAO_INVALIDA = 2;
    private static final int CODIGO_REQUISICAO_ALTERA_NOTA = 1;
    private static final String CHAVE_NOTA = "nota";
    private static final int CODIGO_REQUISICAO_INSERE_NOTA = 12;
    private DatabaseReference mDatabase;
    private List<Post> listPosts;
    public  ListaPostsAdapter adapter;
    private View view;
    private FirebaseAuth mAuth;
    private ListaPostsAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        view = inflater.inflate(R.layout.tela_lista_posts, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.lista_posts_recyclerview);

        mAuth = FirebaseAuth.getInstance();

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

//        configuraItemTouchHelper(listaPosts);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {

        // configura deslize
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new PostItemTouchHelperCallback(adapter));
//        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void getAllPosts(DataSnapshot dataSnapshot){
        listPosts.clear();
        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
            Post a = singleSnapshot.getValue(Post.class);
            listPosts.add(a);
        }
        configuraRecyclerView(listPosts);

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
        adapter.setonRatingChanged(new OnRateChangeListener() {
            @Override
            public void onRateChange(Post post, int posicao, RatingBar ratingBar, float rating, boolean fromUser) {
                    if(fromUser){
                        System.out.println("rate o change");
                        if(post.stars.get(mAuth.getUid())!=null){
                            if(post.stars.get(mAuth.getUid()).intValue() != Math.round(rating)){
                                post.stars.put(mAuth.getUid(),Math.round(rating));
                                mDatabase.child("posts").child(post.getKey()).setValue(post)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Write was successful!
                                                System.out.println("sucesso on change rate");
                                                // ...
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Write failed
                                                // ...
                                                System.out.println("err change rate");

                                            }
                                        });
                            }

                        }
                    }
                }

            });




    }

    public void publicaFacebook(Post post) throws IOException{
//        final File localFile = new File("@drawable/belo_horizonte_mg");

        File localFile = new File("@drawable/belo_horizonte_mg");

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap image = BitmapFactory.decodeFile(localFile.getAbsolutePath(), bmOptions);

        SharePhoto photo = new SharePhoto.Builder()

                .setBitmap(image)

                .setCaption("teste")

                .setImageUrl(Uri.parse("http://www.google.com"))

                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()

                .addPhoto(photo)

                .build();

        ShareDialog.show(getActivity(), content);

//        StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("posts/"+post.getKey());
//        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                Bitmap image = BitmapFactory.decodeFile(localFile.getAbsolutePath(), bmOptions);
//
//                SharePhoto photo = new SharePhoto.Builder()
//
//                        .setBitmap(image)
//
//                        .setCaption("teste")
//
//                        .setImageUrl(Uri.parse("http://www.google.com"))
//
//                        .build();
//
//                SharePhotoContent content = new SharePhotoContent.Builder()
//
//                        .addPhoto(photo)
//
//                        .build();
//
//                ShareDialog.show(getActivity(), content);
//
//            }
//        });

    }

    private void vaiParaFormularioNotaActivityAltera(Post post, int posicao) {
//        Toast.makeText(getContext(), "Teste botao", Toast.LENGTH_SHORT).show();
        try {
            publicaFacebook(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void pegaTodosPosts(){

        System.out.println("ACIONOU PEGA POSTS");
        listPosts  = new ArrayList<Post>();
        // Database listener
        Query query;
        mDatabase.child("posts").orderByChild("aprovado").equalTo(true)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getAllPosts(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
