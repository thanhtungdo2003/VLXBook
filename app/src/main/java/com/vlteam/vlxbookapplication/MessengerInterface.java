package com.vlteam.vlxbookapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vlteam.vlxbookapplication.Adapter.UserAdapter;
import com.vlteam.vlxbookapplication.Adapter.UserAvtImageUnderSearch;
import com.vlteam.vlxbookapplication.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class MessengerInterface extends AppCompatActivity {
    private RecyclerView rcvUser, rcvHorintalUser;
    private List<UserModel> userModelList;
    private UserAdapter userAdapter;
    private UserAvtImageUnderSearch userAvtImageUnderSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_interface);

        // Khởi tạo RecyclerView
        rcvHorintalUser = findViewById(R.id.rcv_horizontal_users);
        rcvUser = findViewById(R.id.rcv_user_list);
        userModelList = new ArrayList<>();
        userAdapter = new UserAdapter(userModelList, (userModel, view) -> {
            Intent intent = new Intent(view.getContext(), ChattingInterface.class);
            intent.putExtra("userImage", userModel.getImageResId());
            intent.putExtra("userName", userModel.getName());
            view.getContext().startActivity(intent);
        });

        userAvtImageUnderSearchAdapter = new UserAvtImageUnderSearch(userModelList, (userModel, view) -> {
            Intent intent = new Intent(view.getContext(), ChattingInterface.class);
            intent.putExtra("userImage", userModel.getImageResId());
            intent.putExtra("userName", userModel.getName());
            view.getContext().startActivity(intent);
        });

        // Kết nối Adapter
        rcvHorintalUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcvUser.setLayoutManager(new LinearLayoutManager(this));
        rcvUser.setAdapter(userAdapter);
        rcvHorintalUser.setAdapter(userAvtImageUnderSearchAdapter);

        // Thêm dữ liệu vào danh sách
        userModelList.clear();
        userModelList.add(new UserModel("1", "Lương", "Đỗ Thanh Tùng óc chó", R.drawable.avt_1, "10:05"));
        userModelList.add(new UserModel("2", "Hòa", "Đỗ Thanh Tùng óc chó", R.drawable.avt_2, "11:20"));
        userModelList.add(new UserModel("3", "Phúc", "Đỗ Thanh Tùng óc chó", R.drawable.avt_3, "14:30"));
        userModelList.add(new UserModel("4", "Nam", "Đỗ Thanh Tùng óc chó", R.drawable.avt_5, "15:45"));
        userModelList.add(new UserModel("5", "Lương", "Đỗ Thanh Tùng óc chó", R.drawable.avt_4, "16:10"));
        userModelList.add(new UserModel("6", "Hòa", "Đỗ Thanh Tùng óc chó", R.drawable.avt_6, "17:55"));
        userModelList.add(new UserModel("7", "Phúc", "Đỗ Thanh Tùng óc chó", R.drawable.avt_7, "18:20"));
        userModelList.add(new UserModel("8", "Nam", "Đỗ Thanh Tùng óc chó", R.drawable.avt_8, "19:05"));
        userModelList.add(new UserModel("9", "Lương", "Đỗ Thanh Tùng óc chó", R.drawable.avt_9, "20:40"));
        userModelList.add(new UserModel("10", "Hòa", "Đỗ Thanh Tùng óc chó", R.drawable.avt_10, "21:15"));
        userModelList.add(new UserModel("11", "Phúc", "Đỗ Thanh Tùng óc chó", R.drawable.avt_3, "22:00"));
        userModelList.add(new UserModel("12", "Nam", "Đỗ Thanh Tùng óc chó", R.drawable.avt_5, "23:30"));

        // Thông báo Adapter cập nhật dữ liệu
        userAdapter.notifyDataSetChanged();
        userAvtImageUnderSearchAdapter.notifyDataSetChanged();
    }
}
