package com.vlteam.vlxbookapplication.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.model.UserInfoModel;

import java.util.List;

public class FriendsProposeBarAdapter extends RecyclerView.Adapter<FriendsProposeBarAdapter.UserViewHolder> {
    private List<UserInfoModel> userModelList;
    private OnItemClickListener onItemClickListener;

    // Interface xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(UserInfoModel userModel, View view);
    }

    // Constructor có thêm listener
    public FriendsProposeBarAdapter(List<UserInfoModel> userModelList, OnItemClickListener listener) {
        this.userModelList = userModelList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_propose, parent, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserInfoModel user = userModelList.get(position);
        if (user == null) return;

        holder.tvFullName.setText(user.getFullName());
        user.renderAvata(holder.imgAvtUser);
        ImageButton open_mess_button = holder.itemView.findViewById(R.id.open_messager_box_fp_btn);
        open_mess_button.setTag(user.getFullName());
        holder.itemView.findViewById(R.id.open_messager_box_fp_btn).setOnClickListener(v -> onItemClickListener.onItemClick(user, v));
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(user, v));
    }

    @Override
    public int getItemCount() {
        return userModelList != null ? userModelList.size() : 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFullName;
        private ImageView imgAvtUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tv_fullname);
            imgAvtUser = itemView.findViewById(R.id.img_avt_user_list);
        }
    }
}

