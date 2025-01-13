package com.vlteam.vlxbookapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class update_infouser_pageActivity extends AppCompatActivity {
    Spinner spinday,spinmonth,spinyear;
    List<Integer> listDay,listMonth,listYear;
    ArrayAdapter<Integer> adapDay,adapMonth,adapYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_infouser_page);
        khoitaoSpinner();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void khoitaoSpinner(){
        spinday = findViewById(R.id.spinDay);
        spinmonth = findViewById(R.id.spinMonth);
        spinyear = findViewById(R.id.spinYear);
        listDay = new ArrayList<>();
        listMonth = new ArrayList<>();
        listYear = new ArrayList<>();
        for(int i=1;i<=31;i++){
            listDay.add(i);
        }
        for (int i=1;i<=12;i++){
            listMonth.add(i);
        }
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        for(int i = (currentYear-150);i<=currentYear;i++){
            listYear.add(i);
        }

        adapDay = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1,listDay);
        adapMonth = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1,listMonth);
        adapYear = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1,listYear);
        adapDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinday.setAdapter(adapDay);
        spinmonth.setAdapter(adapMonth);
        spinyear.setAdapter(adapYear);
        int defaulyear = listYear.size() / 2;
        spinyear.setSelection(defaulyear);
    }
}