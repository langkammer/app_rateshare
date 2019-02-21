package br.com.rateshare.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.helper.FormPostsAdapterHelper;
import br.com.rateshare.model.Categoria;
import br.com.rateshare.model.Post;
import br.com.rateshare.model.User;
import br.com.rateshare.util.DataUtil;
import br.com.rateshare.util.FotoUtil;

public class FormVerPostagemFragment extends Fragment {

    private static final String TAG = "FIREBASE VER POST";

    private View view;

    private FormPostsAdapterHelper helper;

    private String pathFoto;

    public static final String TITULO_APPBAR = "Ver Postagem";

    public static String databaseName = new DatabaseSettings().nameDatabase;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private FirebaseStorage storage;

    private StorageReference mStorageRef;

    private String keyPost = "";

    private ShareDialog shareDialog;

    private CallbackManager callbackManager;

    private Post post;

    private String postKey;

    private Boolean autor;

    public FormVerPostagemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.tela_fragment_formulario_inclui_foto,  container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        getAutorAndKeyPost();

        if(!this.keyPost.equals(null))
            getPostByKey();


        return view;
    }

    private void getAutorAndKeyPost() {
        this.postKey = getArguments().getString("keyPost");

        this.autor = getArguments().getBoolean("autor");
    }


    public void getPostByKey(){
        mDatabase.child("posts/"+this.postKey).addListenerForSingleValueEvent(listnerPost(this.autor));
    }


    public void setPost(Post post){
        this.post = post;
    }

    public ValueEventListener listnerPost(final boolean autor){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Map of all products which has city equal to mCurrentUserCity
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                Post post = dataSnapshot.getValue(Post.class);
                post.setKey(dataSnapshot.getKey());
                helper.preencheFormulario(post,autor);
                setPost(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        return valueEventListener;
    }

    public void putPathFoto(String path) {
        this.pathFoto = path;
    }

    public void setHelper(FormPostsAdapterHelper helper) {
        this.helper = helper;

    }

    private void exibirProgress(boolean exibir) {
        helper.getProgressBar().setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        helper = new FormPostsAdapterHelper(view);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                System.out.println("SUCESSO !!!!");
            }

            @Override
            public void onCancel() {
                System.out.println("CANCELOU !!!!");

            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("ERROOOOOU !!!!");

            }
        });

        setHelper(helper);
        Bundle parametros = getArguments();
        if (parametros != null) {
            String pathFile = (String) parametros.getSerializable("pathFile");
            putPathFoto(pathFile);
            helper.preencheFormulario(new Post(),false);
            helper.carregaImagem(this.pathFoto);

        }

        helper.getForm_item_btn_salvar().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if(helper.validateFields().get("resposta") == "e")
                    Toast.makeText(getContext(), helper.validateFields().get("msg"), Toast.LENGTH_LONG).show();
                else
                    salvarPost();
                }
        });


        helper.getFormItemBtnCamera().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                abreCamera();
            }
        });

        helper.getFormItemOptionCateg().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                Log.d(TAG,"SELE CAT");
                if(post.categoria != null)
                    helper.getFormItemOptionCateg().setSelection(helper.getSpinnerAdapter().getPosicao(post.categoria),true);
                else
                    helper.getFormItemOptionCateg().setSelection(0,true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.d(TAG,"nao selectionou nada");

            }

        });



    }

    private void abreCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = FotoUtil.createImageFile(getContext());
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Error em salvar a foto.", Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "br.com.rateshare.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePictureIntent, FotoUtil.CODIGO_CAMERA);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == FotoUtil.CODIGO_CAMERA) {
                putPathFoto(FotoUtil.caminhoFoto);
            }
        }
    }

    private void salvarPost() {

        this.post.data = DataUtil.data;

        this.post.userKey = mAuth.getCurrentUser().getUid();

        Categoria categoriaSelecionada = (Categoria) helper.getFormItemOptionCateg().getSelectedItem();

        this.post.categoria = categoriaSelecionada;

        HashMap<String, Integer> stars = new HashMap<>();

        stars.put(mAuth.getUid(),helper.getFormItemRate().getProgress());

        this.post.stars =  stars;

        this.post.pathFotoAndroid = this.pathFoto;

        this.post.titulo = helper.getFormItemEitTitulo().getText().toString();

        this.post.descricao = helper.getFormItemTexteditDescript().getText().toString();

        this.post.numAvaliacoes = 1;

        this.post.aprovado = true;

        criaPostagemFirebase();


    }

    public void uploadImage(final Post post){
        // cria referencias da imagem e faz upload
        post.setKey(keyPost);
        StorageReference mountainsRef = mStorageRef.child("posts/" + keyPost);
        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = mStorageRef.child("posts/" + keyPost);

        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        Bitmap bitmap = ((BitmapDrawable) helper.getFormItemImageview().getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        exibirProgress(true);
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                exibirProgress(false);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                publicaFacebook(post);




            }
        });
    }

    public void criaPostagemFirebase(){

        final Post post = this.post;

        if(this.post.getKey()!=null){
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/posts/" + this.post.getKey(), this.post);

            mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    uploadImage(post);
                }
            });

//            mDatabase.child("posts")
//                    .updateChildren()
//                    .setValue(this.post, new DatabaseReference.CompletionListener() {
//                        @Override
//                        public void onComplete(DatabaseError databaseError,
//                                               DatabaseReference databaseReference) {
//                            keyPost = databaseReference.getKey();
//                            uploadImage(post);
//                        }
//                    });
        }else{
            mDatabase.child("posts")
                    .push()
                    .setValue(this.post, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError,
                                               DatabaseReference databaseReference) {
                            keyPost = databaseReference.getKey();
                            uploadImage(post);
                        }
                    });
        }


        Toast.makeText(getContext(), "Post Salvo com Sucesso ! " , Toast.LENGTH_LONG).show();
    }


    public String getStarString(){
        String starString = "";
        if(helper.getFormItemRate().getProgress() == 1)
            starString =  "★";
        if(helper.getFormItemRate().getProgress() == 2)
            starString =  "★★";
        if(helper.getFormItemRate().getProgress() == 3)
            starString =  "★★★";
        if(helper.getFormItemRate().getProgress() == 4)
            starString =  "★★★★";
        if(helper.getFormItemRate().getProgress() == 5)
            starString =  "★★★★★";

        return  starString;
    }

    public void publicaFacebook(Post post){
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("#rateshare").build())
                .setQuote(
                        "  EU avaliei um(a)     \n "                    +
                        post.categoria.nome             + " Com nome : " + post.titulo +
                        "  E a minha avaliação foi:   \n "                    +
                        post.descricao                  +
                        "  e Dei a quantidade estrelas :   \n "                    +
                        getStarString()             +
                        "       \n "                    +
                        " !!!! Avalie você tbm! com o APP RATESHARE !!!! "
                )
                .setContentUrl(Uri.parse("https://rateshareteste.firebaseapp.com/ver-postagem/" + post.getKey()))
                .build();
        shareDialog.show(shareLinkContent);
        exibirProgress(false);
        getActivity().onBackPressed();

    }

}