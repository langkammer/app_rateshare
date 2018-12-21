package br.com.rateshare.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.util.Collections;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.PostModel;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;

public class ListaPostsAdapter extends RecyclerView.Adapter<ListaPostsAdapter.PostViewHolder>{

    private final List<PostModel> posts;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ListaPostsAdapter(Context context, List<PostModel> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListaPostsAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.tela_item_post, parent, false);
        return new PostViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(ListaPostsAdapter.PostViewHolder holder, int position) {
        PostModel post = posts.get(position);
        holder.vincula(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void altera(int posicao, PostModel post) {
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
        private PostModel post;

        public PostViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_post_titulo);
            categoria = itemView.findViewById(R.id.item_post_categoria);
            numAvaliacoes = itemView.findViewById(R.id.item_num_avaliacoes);
            image = itemView.findViewById(R.id.item_post_imagem);
            ratingBar = itemView.findViewById(R.id.item_rating_bar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(post, getAdapterPosition());
                }
            });
        }

        public void vincula(PostModel post) {
            this.post = post;
            preencheCampo(post);
        }

        private void preencheCampo(PostModel post) {
            titulo.setText(post.titulo);
            numAvaliacoes.setText(post.num_avaliacoes);
            categoria.setText(post.nome_categoria);
            ratingBar.setRating(post.nota.intValue());
            File imgFile = new File(post.caminho_foto);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(imageBitmap);
        }
    }

    public void adiciona(PostModel post) {
        posts.add(post);
        notifyDataSetChanged();
    }
}
