# 🤟 Türk İşaret Dili Tanıma — Android Uygulaması

Telefon kamerasından **Türk İşaret Dili (TİD)** kelimelerini **gerçek zamanlı** tanıyan bir Android uygulaması.
MediaPipe ile vücut ve el iskeleti çıkarılır, LSTM tabanlı bir derin öğrenme modeli bu hareket dizisinden **226 farklı kelimeyi** sınıflandırır. Model tamamen cihaz üzerinde (TensorFlow Lite) çalışır; internet bağlantısı gerekmez.

> Bitirme projesi olarak geliştirilmiştir.

<!-- Ekran görüntüleri eklendiğinde:
<p align="center">
  <img src="docs/screenshots/ana_ekran.jpg" width="230">
  <img src="docs/screenshots/tanima.jpg" width="230">
  <img src="docs/screenshots/sozluk.jpg" width="230">
</p>
-->

## ✨ Özellikler
- 📷 **Canlı tanıma:** CameraX ile kameradan görüntü alınır, son 60 kare üzerinden sürekli tahmin yapılır.
- 🦴 **İskelet görselleştirme:** Algılanan el ve vücut noktaları ekranda çizilir (`LandmarkOverlayView`).
- 📊 **Güven skoru ve FPS göstergesi:** %50 eşiğin altındaki tahminler gösterilmez.
- 📖 **Sözlük:** 226 kelimenin Türkçe/İngilizce karşılığı ve örnek işaret videosu.
- 🌙 Açık / koyu tema desteği.

## 🧠 Nasıl Çalışıyor?

```
Kamera ──► MediaPipe Holistic ──► Özellik çıkarımı ──► LSTM (TFLite) ──► Kelime
            (pose + 2 el)         (60 kare × 211)       226 sınıf
```

1. **Landmark çıkarımı:** MediaPipe Holistic her karede 25 vücut noktası ve her el için 21 nokta (x, y, z) verir.
2. **Normalizasyon:** Tüm koordinatlar burun noktasına göre merkezlenir; böylece kişinin kadrajdaki konumu önemsizleşir.
3. **Ek özellikler:** Ellerin ağız, göz, burun ve omuza uzaklıkları ile bir önceki kareye göre el hareket miktarı eklenir → kare başına **211 özellik**.
4. **Sınıflandırma:** 60 karelik dizi 3 katmanlı LSTM modeline verilir.

### Model mimarisi
| Katman | Çıkış |
|---|---|
| LSTM (256) + BatchNorm + Dropout 0.4 | 60 × 256 |
| LSTM (128) + BatchNorm + Dropout 0.4 | 60 × 128 |
| LSTM (64) + BatchNorm + Dropout 0.3 | 64 |
| Dense (128, ReLU) + Dropout 0.3 | 128 |
| Dense (226, Softmax) | 226 |

TFLite boyutu: **~2.9 MB**

## 📈 Sonuçlar
AUTSL test kümesi (2.808 video, 226 sınıf) üzerinde:

| Metrik | Değer |
|---|---|
| Accuracy | **%78.42** |
| F1 (macro) | %78.00 |
| Precision (macro) | %79.97 |
| Recall (macro) | %78.36 |

## 🗂️ Veri Seti
Model, Ankara Üniversitesi tarafından hazırlanan **[AUTSL — Ankara University Turkish Sign Language Dataset](https://cvml.ankara.edu.tr/datasets/)** ile eğitilmiştir (43 kişi, 226 kelime, ~38.000 video).

Veri seti lisansı gereği bu repoda **paylaşılmamıştır**. Sözlük ekranındaki örnek videolar (`app/src/main/res/raw/`) da bu nedenle repoya dahil edilmemiştir; projeyi derlemek için aşağıya bakın.

## 📁 Proje Yapısı
```
android-app/          Android Studio projesi (Java)
  app/src/main/java/com/example/isaretdil/
    MainActivity.java         Kamera, MediaPipe, özellik çıkarımı, TFLite tahmini
    SozlukActivity.java       Kelime sözlüğü
    HomeActivity2.java        Ana ekran
    LandmarkOverlayView.java  İskelet çizimi
  app/src/main/assets/
    isaret_dili_beyni_v2.tflite   Eğitilmiş model
    holistic_landmarker.task      MediaPipe modeli
    SignList_ClassId_TR_EN.csv    Sınıf → kelime eşlemesi
notebooks/            Google Colab defterleri (sırayla çalıştırılır)
  01_koordinat_cikarma_mediapipe.ipynb   Videolardan landmark CSV'si üretme
  02_model_egitimi_v2.ipynb              Uygulamadaki modelin (v2) eğitimi
  03_tflite_donusumu.ipynb               Keras (.h5) → TFLite dönüşümü
  04_test_degerlendirme.ipynb            Test kümesinde metrikler
  05_detayli_degerlendirme.ipynb         Karışıklık matrisi ve sınıf raporları
  deneyler/                              Önceki model denemeleri (100 kare, 201 özellik)
  requirements.txt                       Python bağımlılıkları
```

### Geliştirme süreci
İlk denemede (2 katmanlı LSTM, 100 kare, yalnızca koordinatlar) doğrulama başarısı ~%67'de kaldı. Kare sayısı 60'a indirilip ellerin yüze/omuza uzaklığı ve hareket hızı gibi 10 ek özellik eklendi, model 3 katmanlı LSTM'e büyütüldü. Bu v2 modelde doğrulama başarısı **%81.2**'ye, test başarısı **%78.4**'e çıktı.

## 🚀 Çalıştırma

**Hazır APK:** [Releases](../../releases) sayfasından `app-debug.apk` dosyasını indirip Android telefona kurabilirsiniz (Android 7.0+).

**Kaynaktan derleme:**
1. `android-app/` klasörünü Android Studio ile açın.
2. Sözlük videoları repoda olmadığı için, AUTSL'den her kelime için bir örnek videoyu `app/src/main/res/raw/` içine `v<sınıf_no>_<kelime>.mp4` adıyla (ör. `v0_abla.mp4`) ve ana ekran videolarını `sign_male.mp4` / `sign_female.mp4` olarak koyun.
3. Gerçek bir cihazda çalıştırın (kamera gerekir).

## 🛠️ Kullanılan Teknolojiler
Java · Android (CameraX, Media3/ExoPlayer, Material) · MediaPipe Tasks Vision · TensorFlow / Keras · TensorFlow Lite · Python · Google Colab · scikit-learn · pandas

## 👤 Geliştirici
**Baran Can Demirer** — [GitHub](https://github.com/BaranDemirer)
