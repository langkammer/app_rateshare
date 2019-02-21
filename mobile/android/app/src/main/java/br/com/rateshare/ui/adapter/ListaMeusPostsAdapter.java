package br.com.rateshare.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.rateshare.R;
import br.com.rateshare.model.Post;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;

import static android.view.View.VISIBLE;


public class ListaMeusPostsAdapter extends RecyclerView.Adapter<ListaMeusPostsAdapter.PostViewHolder>{

    private final List<Post> posts;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ListaMeusPostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListaMeusPostsAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.tela_meus_item_post, parent, false);
        return new PostViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaMeusPostsAdapter.PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.vincula(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class PostViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView categoria;
        private final TextView numAvaliacoes;
        private final ImageView image;
        private final ImageView item_post_img_legend;
        private final RatingBar ratingBar;
        private final ImageView imgLegendStatus;
        private Post post;
        private final ProgressBar progressBar;


        public PostViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_post_titulo);
            categoria = itemView.findViewById(R.id.item_post_categoria);
            numAvaliacoes = itemView.findViewById(R.id.item_num_avaliacoes);
            image = itemView.findViewById(R.id.item_post_imagem);
            ratingBar = itemView.findViewById(R.id.item_rating_bar);
            imgLegendStatus  = itemView.findViewById(R.id.img_status_legend);
            progressBar = itemView.findViewById(R.id.item_progressBar);
            item_post_img_legend = itemView.findViewById(R.id.item_post_img_legend);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(post, getAdapterPosition());
                }
            });
        }

        public void vincula(Post post) {
            this.post = post;
            try{
                preencheCampo(post);
            }
            catch (Exception e){
                System.out.println("erro e : "+ e.getMessage());
            }
        }

        private void exibirProgress(boolean exibir) {
            progressBar.setVisibility(exibir ? VISIBLE : View.GONE);
        }
        public float getNota(Map<String, Integer> map){
            int total = 0;
            int soma = 0;
            Set<String> chaves = map.keySet();
            for (String chave : chaves)
            {
                if(chave != null){
                    System.out.println(chave + map.get(chave));
                    total ++;
                    soma += (Integer) map.get(chave);
                }
            }

            return soma / total;
        }

        public String getAvaliacoes(Map<String, Integer> map){
            Integer total = 0;
            Set<String> chaves = map.keySet();
            for (String chave : chaves)
            {
                if(chave != null){
                    total ++;
                }
            }

            return Integer.toString(total);
        }

        private void preencheCampo(Post post) throws IOException {
            titulo.setText(post.titulo);
            numAvaliacoes.setText(getAvaliacoes(post.stars));
            categoria.setText(post.categoria.nome);
//            int nota  = post.stars.get("") / post.stars.size();
            ratingBar.setRating(getNota(post.stars));
            final File localFile = File.createTempFile("image","jpg");
            exibirProgress(true);


            String uriOk = "@drawable/ic_check_black_24dp";

            String uriPendente = "@drawable/com_facebook_close";


            if(post.aprovado){
                int imageResource = context.getResources().getIdentifier(uriOk, null, context.getPackageName());
                Drawable res = context.getResources().getDrawable(imageResource);
                item_post_img_legend.setImageDrawable(res);
                item_post_img_legend.setVisibility(VISIBLE);
            }
            else{
                int imageResource = context.getResources().getIdentifier(uriPendente, null, context.getPackageName());
                Drawable res = context.getResources().getDrawable(imageResource);
                item_post_img_legend.setImageDrawable(res);
                item_post_img_legend.setVisibility(VISIBLE);

            }

            StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("posts/"+post.getKey());
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    exibirProgress(false);

                }
            });

//            File imgFile = new File(post.caminho_foto);
//            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            image.setImageBitmap(imageBitmap);
        }
    }

    public void adiciona(Post post) {
        posts.add(post);
        notifyDataSetChanged();
    }
}
