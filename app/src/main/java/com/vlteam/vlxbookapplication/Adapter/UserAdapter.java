package com.vlteam.vlxbookapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.InfoPage;
import com.vlteam.vlxbookapplication.R;
import com.vlteam.vlxbookapplication.httpservice.ApiService;
import com.vlteam.vlxbookapplication.httpservice.File;
import com.vlteam.vlxbookapplication.httpservice.FileManager;
import com.vlteam.vlxbookapplication.httpservice.RetrofitClient;
import com.vlteam.vlxbookapplication.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserModel> userModelList;
    private OnItemClickListener onItemClickListener;

    // Interface xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(UserModel userModel, View view);
    }

    // Constructor có thêm listener
    public UserAdapter(List<UserModel> userModelList, OnItemClickListener listener) {
        this.userModelList = userModelList;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_message, parent, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userModelList.get(position);
        if (user == null) return;

        holder.tvUserName.setText(user.getFullNameOther());
        holder.tvMessage.setText(user.getContent());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tvTime.setText(user.getTimeOfSend().getHour() + ":" + user.getTimeOfSend().getMinute());
        }

        holder.imgAvtUser.setImageURI(user.getAvataUri());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(user, v));

    }

    @Override
    public int getItemCount() {
        return userModelList != null ? userModelList.size() : 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName, tvMessage, tvTime;
        private ImageView imgAvtUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_fullname);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            imgAvtUser = itemView.findViewById(R.id.img_avt_user_list);
        }
    }
}

