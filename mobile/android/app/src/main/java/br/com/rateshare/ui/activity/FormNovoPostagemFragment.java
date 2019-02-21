package br.com.rateshare.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import br.com.rateshare.R;
import br.com.rateshare.dao.generic.DatabaseSettings;
import br.com.rateshare.helper.FormPostsAdapterHelper;
import br.com.rateshare.model.Categoria;
import br.com.rateshare.model.Post;
import br.com.rateshare.util.DataUtil;
import br.com.rateshare.util.FotoUtil;

import static br.com.rateshare.util.FotoUtil.COMPRESS_QUALIDADE_MEDIA;
import static br.com.rateshare.util.UtilGenerate.gerarChave;

public class FormNovoPostagemFragment extends Fragment {

    private static final String TAG = "FIREBASE NOVO POST";
    private View view;

    private FormPostsAdapterHelper helper;

    private String pathFoto;

    public static final String TITULO_APPBAR = "Nova Postagem";

    public static String databaseName = new DatabaseSettings().nameDatabase;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    private FirebaseStorage storage;

    private StorageReference mStorageRef;

    private String keyPost = "";

    private ShareDialog shareDialog;

    private CallbackManager callbackManager;

    private ExifInterface exifObject;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getView(inflater, container);
    }

    private View getView(LayoutInflater inflater, ViewGroup container) {

        view = inflater.inflate(R.layout.tela_fragment_formulario_inclui_foto,  container, false);

        return view;
    }

    public void putPathFoto(String path) {
        this.pathFoto = path;
        try {
            this.exifObject = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setHelper(FormPostsAdapterHelper helper) {
        this.helper = helper;

    }

    private void exibirProgress(boolean exibir) {
        helper.getProgressBar().setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private void exibirProgress2(boolean exibir) {
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
            helper.preencheFormulario(new Post(),true);
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


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
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

        Post postBean = new Post();

        postBean.data = DataUtil.data;

        postBean.userKey = mAuth.getCurrentUser().getUid();

        Categoria categoriaSelecionada = (Categoria) helper.getFormItemOptionCateg().getSelectedItem();

        postBean.categoria = categoriaSelecionada;

        HashMap<String, Integer> stars = new HashMap<>();

        stars.put(mAuth.getUid(),helper.getFormItemRate().getProgress());

        postBean.stars =  stars;

        postBean.pathFotoAndroid = this.pathFoto;

        postBean.titulo = helper.getFormItemEitTitulo().getText().toString();

        postBean.descricao = helper.getFormItemTexteditDescript().getText().toString();

        postBean.numAvaliacoes = 1;

        postBean.aprovado = true;

        criaPostagemFirebase(postBean);


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

        int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap imageRotate = rotateBitmap(bitmap,orientation);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        imageRotate.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALIDADE_MEDIA, baos);


        helper.getProgressBar2().setVisibility(View.VISIBLE);

        byte[] data = baos.toByteArray();
        exibirProgress(true);
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                exibirProgress(false);

            }
        })
        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                System.out.println("Upload is " + progress + "% done");
                int currentprogress = (int) progress;
                helper.getProgressBar2().setProgress(currentprogress);
            }
        })
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                publicaFacebook(post);
            }
        });
    }

    public void criaPostagemFirebase(final Post post){

        mDatabase.child("posts")
                .child(gerarChave())
                .setValue(post, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {
                         keyPost = databaseReference.getKey();
                         uploadImage(post);
                    }
                });

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
    private String gerarCaptolPostFacebook(Post post){
        String retorno =  "  EU avaliei um(a)     \n "                    +
                post.categoria.nome             + " Com nome : " + post.titulo +
                "  E a minha avaliação foi:   \n "                    +
                post.descricao                  +
                "  e Dei a quantidade estrelas :   \n "                    +
                getStarString()             +
                "       \n "                    +
                " !!!! Avalie você tbm! com o APP RATESHARE !!!! ";

        return retorno;
    }

    public void publicaFacebook(Post post){
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setShareHashtag(new ShareHashtag.Builder().setHashtag("#rateshare").build())
                .setQuote(gerarCaptolPostFacebook(post))
                .setContentUrl(Uri.parse("https://rateshareteste.firebaseapp.com/postbyid/" + post.getKey()))
                .build();
        shareDialog.show(shareLinkContent);
        exibirProgress(false);
        helper.getProgressBar2().setVisibility(View.INVISIBLE);
        getActivity().onBackPressed();

    }

}