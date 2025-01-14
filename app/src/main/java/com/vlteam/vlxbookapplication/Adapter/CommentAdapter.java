package com.vlteam.vlxbookapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.NewfeedActivity;
import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.model.CommentModel;
import com.vlteam.vlxbookapplication.model.MessageModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<CommentModel> commentList;

    public CommentAdapter(List<CommentModel> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentModel comment = commentList.get(position);
        holder.tvUserName.setText(comment.getUserName());
        holder.tvCommentContent.setText(comment.getComment());
        holder.tvTime.setText(comment.getTimeAgo()+"p");
        holder.imgAvatar.setImageResource(comment.getAvatar());

        // Hiển thị avatar nếu có
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvCommentContent, tvTime;
         ImageView imgAvatar;

        public CommentViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name_comment);
            tvCommentContent = itemView.findViewById(R.id.tv_comment_content);
            tvTime = itemView.findViewById(R.id.tv_time_comment);
            imgAvatar = itemView.findViewById(R.id.img_avt_user_coment);
        }
    }
}


