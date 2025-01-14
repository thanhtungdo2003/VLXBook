package com.vlteam.vlxbookapplication.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class UserAvtImageUnderSearch extends RecyclerView.Adapter<UserAvtImageUnderSearch.UserViewHolder> {
    private List<UserModel> userModelList;
    private OnItemClickListener onItemClickListener;
    private ApiService apiService;
    private FileManager fileManager;
    public interface OnItemClickListener {
        void onItemClick(UserModel userModel, View view);
    }

    public UserAvtImageUnderSearch(List<UserModel> userModelList, OnItemClickListener onItemClickListener) {
        this.userModelList = userModelList;
        this.onItemClickListener = onItemClickListener;
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_avt_name_under_search, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userModelList.get(position);
        if (user == null) return;
        holder.tvNameUnderSearch.setText(user.getFullNameOther());
        user.renderAvata(holder.imgAvtUnderSearch);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(user, v));
    }

    @Override
    public int getItemCount() {
        return userModelList != null ? userModelList.size() : 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameUnderSearch;
        private ImageView imgAvtUnderSearch;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvtUnderSearch = itemView.findViewById(R.id.img_user_avatar_under_search);
            tvNameUnderSearch = itemView.findViewById(R.id.tv_user_name_under_search);
        }
    }
}
