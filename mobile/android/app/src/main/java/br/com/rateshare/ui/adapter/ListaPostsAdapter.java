package br.com.rateshare.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import br.com.rateshare.R;
import br.com.rateshare.model.Post;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;
import br.com.rateshare.ui.adapter.listener.OnRateChangeListener;

public class ListaPostsAdapter extends RecyclerView.Adapter<ListaPostsAdapter.PostViewHolder>{

    private final List<Post> posts;
    private final Context context;
    private OnItemClickListener onItemClickListener;
    private OnRateChangeListener onRatingBarChange;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public ListaPostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setonRatingChanged(OnRateChangeListener onRatingBarChangeListener) {
        this.onRatingBarChange =  onRatingBarChangeListener;
    }

    @Override
    public ListaPostsAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.tela_item_post, parent, false);
        return new PostViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaPostsAdapter.PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.vincula(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void altera(int posicao, Post post) {
        posts.set(posicao, post);
        notifyDataSetChanged();
    }

    public void remove(int posicao) {
        posts.remove(posicao);
        notifyItemRemoved(posicao);
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(posts, posicaoInicial, posicaoFinal);
        notifyItemMoved(posicaoInicial, posicaoFinal);
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView categoria;
        private final TextView numAvaliacoes;
        private final ImageView image;
        private final RatingBar ratingBar;
        private final ProgressBar progressBar;
        private Post post;

        private Bitmap bitmapImg;

        public PostViewHolder(View itemView) {
            super(itemView);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mAuth = FirebaseAuth.getInstance();

            titulo = itemView.findViewById(R.id.item_post_titulo);
            categoria = itemView.findViewById(R.id.item_post_categoria);
            numAvaliacoes = itemView.findViewById(R.id.item_num_avaliacoes);
            image = itemView.findViewById(R.id.item_post_imagem);
            ratingBar = itemView.findViewById(R.id.item_rating_bar);
            progressBar = itemView.findViewById(R.id.item_progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(post, getAdapterPosition());
                }
            });

            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBr, float rating, boolean fromUser) {
                    onRatingBarChange.onRateChange(post,getAdapterPosition(),ratingBar,ratingBar.getRating(),fromUser,bitmapImg);
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
            progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
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
            StorageReference islandRef = FirebaseStorage.getInstance().getReference().child("posts/"+post.getKey());
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    image.setImageBitmap(bitmap);
                    bitmapImg = bitmap;
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
