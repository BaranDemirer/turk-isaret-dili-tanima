package com.example.isaretdil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.isaretdil.databinding.ActivityHome2Binding;

public class HomeActivity2 extends AppCompatActivity {

    private ActivityHome2Binding binding;
    private SharedPreferences prefs;

    // Durumlar
    private boolean isSettingsOpen = false;
    private boolean isFemale = false;
    private boolean isDark = false;
    private boolean isVideoPlaying = false;

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_DARK = "dark_theme";
    private static final String KEY_GENDER = "gender_female";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. ÖNCE SİSTEM OLUŞTURULMALI (Çökme buranın altta kalmasından oluyordu)
        super.onCreate(savedInstanceState);

        // 2. Ekranı dikey modda kilitle
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 3. Hafızadan tema ve cinsiyet verilerini çek (Artık çökmez çünkü sayfa oluştu)
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        isDark = prefs.getBoolean(KEY_DARK, false);
        isFemale = prefs.getBoolean(KEY_GENDER, false);

        // 4. Temayı uygula
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // 5. Ekran tasarımını (XML) Java'ya bağla
        binding = ActivityHome2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Arayüz hazırlığı
        updateGenderUI();
        setupClickListeners();
        setupInitialVideo();
    }

    private void setupClickListeners() {
        // ⚙️ Ayarlar Butonu (En Sağdaki Yuvarlak)
        binding.btnSettings.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
            if (isSettingsOpen) closeSettingsMenu(); else openSettingsMenu();
        });

        // ♂/♀ Cinsiyet Butonu (Sola doğru fırlayan 1. buton)
        binding.btnGenderToggle.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
            isFemale = !isFemale;
            prefs.edit().putBoolean(KEY_GENDER, isFemale).apply();
            updateGenderUI();
            playSignVideo();
        });

        // 🌗 Tema Butonu (Sola doğru fırlayan 2. buton)
        binding.btnThemeToggle.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
            isDark = !isDark;
            prefs.edit().putBoolean(KEY_DARK, isDark).apply();

            if (isDark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // 📚 Kelimeler Butonu
        binding.btnLearn.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            startActivity(new Intent(HomeActivity2.this, SozlukActivity.class));
        });

        // 🎯 Test Et Butonu (Yapay Zekalı Kamera Sayfasına Gider)
        binding.btnPractice.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            startActivity(new Intent(HomeActivity2.this, MainActivity.class));
        });

        // 📺 Videoya dokunulduğunda oynat/duraklat
        binding.cardVideo.setOnClickListener(v -> toggleVideoPlayback());

        // Menü açıkken boşluğa basınca kapat
        binding.rootLayout.setOnClickListener(v -> {
            if (isSettingsOpen) closeSettingsMenu();
        });
    }

    // 🌟 AYARLAR MENÜSÜNÜ SOLA DOĞRU AÇAN ANİMASYON 🌟
    private void openSettingsMenu() {
        isSettingsOpen = true;

        // Ana buton 180 derece döner
        binding.btnSettings.animate().rotation(180f).setDuration(400).start();

        // Butonları görünür yap
        binding.btnThemeToggle.setVisibility(View.VISIBLE);
        binding.btnGenderToggle.setVisibility(View.VISIBLE);

        // Apple stili yaylanma efekti (Overshoot) ile sola fırlat
        // Mesafe: -65dp ve -120dp (Ekrana göre hesaplanır)
        float density = getResources().getDisplayMetrics().density;

        binding.btnThemeToggle.animate()
                .translationX(-65 * density)
                .alpha(1f)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start();

        binding.btnGenderToggle.animate()
                .translationX(-120 * density)
                .alpha(1f)
                .setDuration(450)
                .setInterpolator(new OvershootInterpolator(1.2f))
                .start();
    }

    private void closeSettingsMenu() {
        isSettingsOpen = false;

        binding.btnSettings.animate().rotation(0f).setDuration(400).start();

        // Butonları tekrar merkeze (0) çek ve şeffaf yap
        binding.btnThemeToggle.animate().translationX(0).alpha(0).setDuration(300).start();
        binding.btnGenderToggle.animate().translationX(0).alpha(0).setDuration(300)
                .withEndAction(() -> {
                    binding.btnThemeToggle.setVisibility(View.INVISIBLE);
                    binding.btnGenderToggle.setVisibility(View.INVISIBLE);
                }).start();
    }

    private void updateGenderUI() {
        if (isFemale) {
            binding.tvGenderBadge.setText("Model: Çocuk (Kız)");
        } else {
            binding.tvGenderBadge.setText("Model: Çocuk (Erkek)");
        }
    }

    private void setupInitialVideo() {
        playSignVideo();
    }

    private void playSignVideo() {
        try {
            // raw klasöründeki sign_female veya sign_male videolarını oynatır
            int videoRes = isFemale ? R.raw.sign_female : R.raw.sign_male;
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + videoRes);

            binding.videoView.setVideoURI(uri);
            binding.videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true); // Giriş animasyonu sürekli dönsün
                binding.videoPlaceholder.setVisibility(View.GONE);
                binding.videoView.start();
                isVideoPlaying = true;
                binding.ivPlayOverlay.setVisibility(View.GONE);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleVideoPlayback() {
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
            isVideoPlaying = false;
            binding.ivPlayOverlay.setVisibility(View.VISIBLE);
        } else {
            binding.videoView.start();
            isVideoPlaying = true;
            binding.ivPlayOverlay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVideoPlaying) binding.videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (binding.videoView.isPlaying()) binding.videoView.pause();
    }
}