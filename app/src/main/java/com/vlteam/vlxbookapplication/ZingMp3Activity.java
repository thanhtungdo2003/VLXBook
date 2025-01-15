package com.vlteam.vlxbookapplication;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ZingMp3Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_zing_mp3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
            // Khởi tạo WebView

        });

        WebView webView = findViewById(R.id.wv_zingmp3);

        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://l.facebook.com/l.php?u=https%3A%2F%2Fzingmp3.vn%2Ftim-kiem%2Ftat-ca%3Fq%3Dt%25E1%25BA%25BFt%2520b%25C3%25ACnh%2520an%26fbclid%3DIwZXh0bgNhZW0CMTAAAR0K3fCY97E11DBp8aVDXJQauqUSsVVZtfqEdS_4dlP8UgOh-utlXUNBkWg_aem_IPi5icdfB0Weuc4gdwuxfQ&h=AT0nl7WoWCW_zEfp6FZ1xLjgMitDhqMTaxEVMVG0UyHE4Zc_pSpZstDSTPuzIo32iSKzOvAxJTZ30y4IgkaYkCsBXuDz2aZo26XENdZrtb1fYay2cF8b7bmCA7zpFqHROQapdbWYZdE-ja6NRSX_cg");
    }
}