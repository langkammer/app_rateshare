package br.com.rateshare.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.rateshare.R;
import br.com.rateshare.model.Post;
import br.com.rateshare.ui.adapter.listener.OnItemClickListener;
import br.com.rateshare.util.ResourceUtil;

public class ListaPostsAdapter extends RecyclerView.Adapter<ListaPostsAdapter.PostViewHolder>{

    private final List<Post> posts;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ListaPostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ListaPostsAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context)
                .inflate(R.layout.activity_item_post, parent, false);
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
        private Post post;

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

        public void vincula(Post post) {
            this.post = post;
            preencheCampo(post);
        }

        private void preencheCampo(Post post) {
            titulo.setText(post.getTitulo());
            numAvaliacoes.setText(post.getNumAvaliacoes());
            categoria.setText(post.getCategoria());
            ratingBar.setRating(Integer.parseInt(post.getRate()));
            Drawable drawableImagemPacote = ResourceUtil
                    .devolveDrawable(context, post.getImagem());
            image.setImageDrawable(drawableImagemPacote);
        }
    }

    public void adiciona(Post post) {
        posts.add(post);
        notifyDataSetChanged();
    }
}
