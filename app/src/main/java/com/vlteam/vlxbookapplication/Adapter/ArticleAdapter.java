package com.vlteam.vlxbookapplication.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.model.Article;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> postList;
    private OnItemClickListener listener;
    public ArticleAdapter(List<Article> postList) {
        this.postList = postList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ArticleViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article art = postList.get(position);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy (HH:mm)");

        holder.tvDateOfPost.setText(art.getTimeOfPost().format(formatter));
        holder.tvNameUser.setText(art.getFullName());
        holder.tvCaption.setText(art.getCaption());
        art.renderImg(holder.imgItemimg, art.getImgArray()[0]);
        holder.btnNext.setOnClickListener(v ->{
            if(listener!=null){
                listener.Onclick(position,art);
            }
        });
        holder.btnComment.setOnClickListener(v -> {
            if(listener != null){
                listener.OnCommentClick(position, art);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNameUser, tvDateOfPost, tvCaption;
        public ImageView imgAvatar, imgItemimg,btnShare;
        public Button btnLike, btnComment ;
        public ImageButton btnNext;
        public ArticleViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.circleImageView);
            imgItemimg = itemView.findViewById(R.id.imgItemimg);
            tvNameUser = itemView.findViewById(R.id.tvNameUser);
            tvDateOfPost = itemView.findViewById(R.id.tvDateOfPost);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            btnLike = itemView.findViewById(R.id.btn_like);
            btnComment = itemView.findViewById(R.id.btn_view_cmt);
            btnShare = itemView.findViewById(R.id.btn_share_post);
            btnNext = itemView.findViewById(R.id.btnNextIMG);
        }
    }

    public interface OnItemClickListener {
        void Onclick(int position,Article article);
        void OnCommentClick(int position, Article article);
    }
}